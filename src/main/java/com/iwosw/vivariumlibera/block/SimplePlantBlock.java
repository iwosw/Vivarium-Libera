package com.iwosw.vivariumlibera.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class SimplePlantBlock extends StackablePlantBlock {
    public static final MapCodec<SimplePlantBlock> CODEC = simpleCodec(SimplePlantBlock::new);

    public SimplePlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public SimplePlantBlock(BlockBehaviour.Properties properties, boolean requiresNearbyWater) {
        super(properties, requiresNearbyWater);
    }

    @Override
    public MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }
}
