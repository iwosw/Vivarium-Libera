package com.iwosw.vivariumlibera.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public final class SimplePlantBlock extends BushBlock {
    public static final MapCodec<SimplePlantBlock> CODEC = simpleCodec(SimplePlantBlock::new);
    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");

    private final boolean requiresNearbyWater;

    public SimplePlantBlock(BlockBehaviour.Properties properties) {
        this(properties, false);
    }

    public SimplePlantBlock(BlockBehaviour.Properties properties, boolean requiresNearbyWater) {
        super(properties);
        this.requiresNearbyWater = requiresNearbyWater;
        this.registerDefaultState(this.defaultBlockState().setValue(BLOOMING, true));
    }

    @Override
    public MapCodec<SimplePlantBlock> codec() {
        return CODEC;
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOOMING);
    }
}
