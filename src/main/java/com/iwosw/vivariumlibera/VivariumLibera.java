package com.iwosw.vivariumlibera;

import com.iwosw.vivariumlibera.registry.ModBlockEntities;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import com.iwosw.vivariumlibera.effect.ModEffects;
import com.iwosw.vivariumlibera.registry.ModCreativeTabs;
import com.iwosw.vivariumlibera.registry.ModDataComponents;
import com.iwosw.vivariumlibera.registry.ModFeatures;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModMenuTypes;
import com.iwosw.vivariumlibera.registry.ModRecipes;
import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.EffectCures;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.slf4j.Logger;

@Mod(VivariumLibera.MOD_ID)
public final class VivariumLibera {
    public static final String MOD_ID = "vivariumlibera";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VivariumLibera(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModEffects.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        NeoForge.EVENT_BUS.addListener(this::keepPoisonAfterMilk);

        LOGGER.info("Initializing {}", MOD_ID);
    }

    private void keepPoisonAfterMilk(MobEffectEvent.Remove event) {
        if (event.getCure() == EffectCures.MILK && event.getEffect().equals(MobEffects.POISON)) {
            event.setCanceled(true);
        }
    }
}
