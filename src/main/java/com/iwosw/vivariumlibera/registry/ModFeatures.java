package com.iwosw.vivariumlibera.registry;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.worldgen.feature.StreamFeature;
import com.iwosw.vivariumlibera.worldgen.feature.StreamFeatureConfiguration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, VivariumLibera.MOD_ID);

    public static final DeferredHolder<Feature<?>, StreamFeature> STREAM = FEATURES.register(
            "stream",
            () -> new StreamFeature(StreamFeatureConfiguration.CODEC)
    );

    private ModFeatures() {
    }

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}
