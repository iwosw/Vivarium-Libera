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

    public SimplePlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BLOOMING);
    }
}
