package com.iwosw.vivariumlibera.block.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum AlchemyLiquid implements StringRepresentable {
    NONE("none"),
    WATER("water"),
    OIL("oil"),
    WINE("wine");

    public static final Codec<AlchemyLiquid> CODEC = StringRepresentable.fromEnum(AlchemyLiquid::values);
    public static final StreamCodec<ByteBuf, AlchemyLiquid> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(
            index -> values()[Math.floorMod(index, values().length)],
            AlchemyLiquid::ordinal);

    private final String name;

    AlchemyLiquid(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public static AlchemyLiquid byName(String name) {
        for (AlchemyLiquid liquid : values()) {
            if (liquid.name.equals(name)) {
                return liquid;
            }
        }
        return NONE;
    }
}
