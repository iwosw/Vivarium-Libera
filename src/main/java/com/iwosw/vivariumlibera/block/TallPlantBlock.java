package com.iwosw.vivariumlibera.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public final class TallPlantBlock extends DoublePlantBlock {
    public static final MapCodec<TallPlantBlock> CODEC = simpleCodec(TallPlantBlock::new);
    public static final BooleanProperty BLOOMING = BooleanProperty.create("blooming");

    public TallPlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(BLOOMING, true));
    }

    @Override
    public MapCodec<TallPlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide && state.getValue(HALF) == DoubleBlockHalf.LOWER && level.isEmptyBlock(pos.above())) {
            level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(BLOOMING) && state.getValue(HALF) == DoubleBlockHalf.LOWER && level.isEmptyBlock(pos.above())) {
            BlockState bloomingState = state.setValue(BLOOMING, true);
            level.setBlock(pos, bloomingState, 3);
            level.setBlock(pos.above(), bloomingState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BLOOMING);
    }
}
