package com.iwosw.vivariumlibera.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record StreamFeatureConfiguration(
        int cellSizeBlocks,
        float cellChance,
        int minY,
        int maxY,
        int maxLocalSlope,
        int radius,
        int depth,
        boolean debugMarkers
) implements FeatureConfiguration {
    public static final Codec<StreamFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.intRange(32, 256).fieldOf("cell_size_blocks").forGetter(StreamFeatureConfiguration::cellSizeBlocks),
            Codec.floatRange(0.0F, 1.0F).fieldOf("cell_chance").forGetter(StreamFeatureConfiguration::cellChance),
            Codec.INT.fieldOf("min_y").forGetter(StreamFeatureConfiguration::minY),
            Codec.INT.fieldOf("max_y").forGetter(StreamFeatureConfiguration::maxY),
            Codec.intRange(0, 16).fieldOf("max_local_slope").forGetter(StreamFeatureConfiguration::maxLocalSlope),
            Codec.intRange(0, 3).fieldOf("radius").forGetter(StreamFeatureConfiguration::radius),
            Codec.intRange(1, 3).fieldOf("depth").forGetter(StreamFeatureConfiguration::depth),
            Codec.BOOL.optionalFieldOf("debug_markers", false).forGetter(StreamFeatureConfiguration::debugMarkers)
    ).apply(instance, StreamFeatureConfiguration::new));
}
