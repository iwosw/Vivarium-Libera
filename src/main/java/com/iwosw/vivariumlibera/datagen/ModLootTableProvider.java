package com.iwosw.vivariumlibera.datagen;

import com.iwosw.vivariumlibera.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModLootTableProvider {
    public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        return new LootTableProvider(
                output,
                Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(
                                ModBlockLootSubProvider::new,
                                LootContextParamSets.BLOCK
                        )
                ),
                lookupProvider
        );
    }

    public static class ModBlockLootSubProvider extends BlockLootSubProvider {
        protected ModBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        @Override
        protected void generate() {
            // Drop self for standard blocks
            dropSelf(ModBlocks.PLUM_PLANKS.get());
            dropSelf(ModBlocks.PLUM_STAIRS.get());
            add(ModBlocks.PLUM_SLAB.get(), block -> createSlabItemTable(block));
            dropSelf(ModBlocks.PLUM_BUTTON.get());
            dropSelf(ModBlocks.PLUM_PRESSURE_PLATE.get());
            dropSelf(ModBlocks.PLUM_FENCE.get());
            dropSelf(ModBlocks.PLUM_FENCE_GATE.get());
            dropSelf(ModBlocks.PLUM_TRAPDOOR.get());
            dropSelf(ModBlocks.PLUM_LOG.get());
            dropSelf(ModBlocks.STRIPPED_PLUM_LOG.get());
            dropSelf(ModBlocks.PLUM_SAPLING.get());
            dropSelf(ModBlocks.MORTAR.get());
            dropSelf(ModBlocks.ALCHEMY_TABLE.get());
            dropSelf(ModBlocks.JUG.get());
            dropSelf(ModBlocks.WATER_JUG_EMPTY.get());
            dropSelf(ModBlocks.WATER_JUG_FULL.get());
            dropSelf(ModBlocks.OIL_JUG_EMPTY.get());
            dropSelf(ModBlocks.OIL_JUG_FULL.get());
            dropSelf(ModBlocks.WINE_JUG_EMPTY.get());
            dropSelf(ModBlocks.WINE_JUG_FULL.get());

            // Plants
            dropSelf(ModBlocks.WORMWOOD.get());
            dropSelf(ModBlocks.NETTLE.get());
            dropSelf(ModBlocks.HENBANE.get());
            dropSelf(ModBlocks.ST_JOHNS_WORT.get());
            dropSelf(ModBlocks.DATURA.get());
            dropSelf(ModBlocks.CALAMUS.get());
            dropSelf(ModBlocks.CALENDULA.get());
            dropSelf(ModBlocks.COMFREY.get());
            dropSelf(ModBlocks.EYEBRIGHT.get());
            dropSelf(ModBlocks.SAGE.get());
            dropSelf(ModBlocks.YARROW_WHITE.get());
            dropSelf(ModBlocks.YARROW_RED.get());
            dropSelf(ModBlocks.YARROW_PINK.get());
            dropSelf(ModBlocks.YARROW_YELLOW.get());
            dropSelf(ModBlocks.LYCORIS.get());
            dropSelf(ModBlocks.CROWS_EYE.get());
            dropSelf(ModBlocks.THISTLE.get());
            dropSelf(ModBlocks.MINT.get());

            dropSelf(ModBlocks.WOOD_SORREL_RED.get());
            dropSelf(ModBlocks.WOOD_SORREL_YELLOW.get());

            // Tall plants (Double plant blocks drop when they are broken)
            add(ModBlocks.FIREWEED.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.FIREWEED.get()));
            add(ModBlocks.CHICORY.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.CHICORY.get()));
            add(ModBlocks.CATTAIL.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.CATTAIL.get()));
            add(ModBlocks.BELL_FLOWER_BLUE.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.BELL_FLOWER_BLUE.get()));
            add(ModBlocks.BELL_FLOWER_PINK.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.BELL_FLOWER_PINK.get()));
            add(ModBlocks.BELL_FLOWER_VIOLET.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.BELL_FLOWER_VIOLET.get()));
            add(ModBlocks.BELL_FLOWER_WHITE.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.BELL_FLOWER_WHITE.get()));
            add(ModBlocks.VALERIAN_WHITE.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.VALERIAN_WHITE.get()));
            add(ModBlocks.VALERIAN_PINK.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.VALERIAN_PINK.get()));
            add(ModBlocks.VALERIAN_RED.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.VALERIAN_RED.get()));
            add(ModBlocks.BELLADONNA.get(), block -> createDoublePlantWithSeedDrops(block, ModBlocks.BELLADONNA.get()));

            // Leaves (Drop saplings, sticks)
            add(ModBlocks.PLUM_LEAVES.get(), block -> createLeavesDrops(block, ModBlocks.PLUM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
            add(ModBlocks.PLUM_LEAVES_1.get(), block -> createLeavesDrops(block, ModBlocks.PLUM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
            add(ModBlocks.PLUM_LEAVES_2.get(), block -> createLeavesDrops(block, ModBlocks.PLUM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.get())::iterator;
        }
    }
}
