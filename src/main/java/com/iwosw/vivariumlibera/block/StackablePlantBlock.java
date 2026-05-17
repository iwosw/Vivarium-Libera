package com.iwosw.vivariumlibera.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class StackablePlantBlock extends BushBlock {
    public static final MapCodec<StackablePlantBlock> CODEC = simpleCodec(StackablePlantBlock::new);
    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 1, 2);
    public static final BooleanProperty ALT = BooleanProperty.create("alt");
    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final int MAX_AMOUNT = 2;
    private static final VoxelShape SINGLE_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 15.0, 14.0);
    private static final VoxelShape DOUBLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    private final boolean requiresNearbyWater;

    public StackablePlantBlock(BlockBehaviour.Properties properties) {
        this(properties, false);
    }

    public StackablePlantBlock(BlockBehaviour.Properties properties, boolean requiresNearbyWater) {
        super(properties);
        this.requiresNearbyWater = requiresNearbyWater;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(AMOUNT, 1)
                .setValue(ALT, false)
                .setValue(BLOOMING, true)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public MapCodec<StackablePlantBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState clickedState = context.getLevel().getBlockState(context.getClickedPos());
        if (clickedState.is(this)) {
            return clickedState.setValue(AMOUNT, Math.min(MAX_AMOUNT, clickedState.getValue(AMOUNT) + 1));
        }

        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(BLOOMING, true)
                .setValue(ALT, useAltVariant(context.getClickedPos()));
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        if (!useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem())) {
            return state.getValue(AMOUNT) < MAX_AMOUNT;
        }

        return super.canBeReplaced(state, useContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AMOUNT, ALT, BLOOMING, FACING);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(BLOOMING)) {
            level.setBlock(pos, state.setValue(BLOOMING, true), 3);
        }
    }

    @Override
    protected boolean canSurvive(BlockState state, net.minecraft.world.level.LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos)
                && (!requiresNearbyWater || PlantPlacementRules.hasNearbyWater(level, pos));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Vec3 offset = state.getOffset(level, pos);
        VoxelShape shape = state.getValue(AMOUNT) == MAX_AMOUNT ? DOUBLE_SHAPE : SINGLE_SHAPE;
        return shape.move(offset.x, offset.y, offset.z);
    }

    private static boolean useAltVariant(BlockPos pos) {
        long seed = pos.asLong();
        return ((seed ^ (seed >>> 17)) & 1L) != 0L;
    }
}
