package com.iwosw.vivariumlibera;

import com.iwosw.vivariumlibera.registry.ModBlocks;
import com.iwosw.vivariumlibera.registry.ModCreativeTabs;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(VivariumLibera.MOD_ID)
public final class VivariumLibera {
    public static final String MOD_ID = "vivariumlibera";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VivariumLibera(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        LOGGER.info("Initializing {}", MOD_ID);
    }
}
