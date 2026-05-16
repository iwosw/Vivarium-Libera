package com.iwosw.vivariumlibera.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class SimplePlantBlock extends BushBlock {
    public static final MapCodec<SimplePlantBlock> CODEC = simpleCodec(SimplePlantBlock::new);

    public SimplePlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<SimplePlantBlock> codec() {
        return CODEC;
    }
}
