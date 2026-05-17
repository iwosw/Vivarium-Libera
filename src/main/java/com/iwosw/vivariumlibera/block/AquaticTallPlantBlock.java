package com.iwosw.vivariumlibera.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public final class AquaticTallPlantBlock extends DoublePlantBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<AquaticTallPlantBlock> CODEC = simpleCodec(AquaticTallPlantBlock::new);
    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AquaticTallPlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(BLOOMING, true)
                .setValue(WATERLOGGED, false));
    }

    @Override
    public MapCodec<AquaticTallPlantBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        }

        return state.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!super.canSurvive(state, level, pos)) {
            return false;
        }

        BlockPos lowerPos = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
        return PlantPlacementRules.hasWaterAtOrNearby(level, lowerPos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (level.isClientSide || state.getValue(HALF) != DoubleBlockHalf.LOWER) {
            return;
        }

        BlockPos upperPos = pos.above();
        if (level.isEmptyBlock(upperPos) || level.getFluidState(upperPos).is(Fluids.WATER)) {
            level.setBlock(upperPos, state
                    .setValue(HALF, DoubleBlockHalf.UPPER)
                    .setValue(WATERLOGGED, level.getFluidState(upperPos).is(Fluids.WATER)), 3);
        }
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos currentPos,
            BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(BLOOMING) && state.getValue(HALF) == DoubleBlockHalf.LOWER && level.isEmptyBlock(pos.above())) {
            BlockState bloomingState = state.setValue(BLOOMING, true);
            level.setBlock(pos, bloomingState, 3);
            level.setBlock(pos.above(), bloomingState
                    .setValue(HALF, DoubleBlockHalf.UPPER)
                    .setValue(WATERLOGGED, level.getFluidState(pos.above()).is(Fluids.WATER)), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BLOOMING, WATERLOGGED);
    }
}
