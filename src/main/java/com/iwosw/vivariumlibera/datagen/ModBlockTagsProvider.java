package com.iwosw.vivariumlibera.datagen;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VivariumLibera.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.MORTAR.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.PLUM_PLANKS.get())
                .add(ModBlocks.PLUM_LOG.get())
                .add(ModBlocks.STRIPPED_PLUM_LOG.get())
                .add(ModBlocks.PLUM_SLAB.get())
                .add(ModBlocks.PLUM_STAIRS.get())
                .add(ModBlocks.PLUM_FENCE.get())
                .add(ModBlocks.PLUM_FENCE_GATE.get())
                .add(ModBlocks.PLUM_TRAPDOOR.get())
                .add(ModBlocks.ALCHEMY_TABLE.get());

        tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.PLUM_LOG.get())
                .add(ModBlocks.STRIPPED_PLUM_LOG.get());

        tag(BlockTags.PLANKS)
                .add(ModBlocks.PLUM_PLANKS.get());

        tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.PLUM_FENCE.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.PLUM_FENCE_GATE.get());

        tag(BlockTags.SAPLINGS)
                .add(ModBlocks.PLUM_SAPLING.get());

        tag(BlockTags.LEAVES)
                .add(ModBlocks.PLUM_LEAVES.get())
                .add(ModBlocks.PLUM_LEAVES_1.get())
                .add(ModBlocks.PLUM_LEAVES_2.get());
    }
}
