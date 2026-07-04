package com.iwosw.vivariumlibera.datagen;

import com.iwosw.vivariumlibera.VivariumLibera;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = VivariumLibera.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper mockHelper = new MockExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Server providers
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(event.includeServer(), ModLootTableProvider.create(packOutput, lookupProvider));

        ModBlockTagsProvider blockTagsProvider = generator.addProvider(event.includeServer(),
                new ModBlockTagsProvider(packOutput, lookupProvider, mockHelper));
        generator.addProvider(event.includeServer(),
                new ModItemTagsProvider(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), mockHelper));

        // Client providers
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput, mockHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput, mockHelper));
    }
}
