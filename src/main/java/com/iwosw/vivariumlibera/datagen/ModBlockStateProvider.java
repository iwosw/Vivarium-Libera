package com.iwosw.vivariumlibera.datagen;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, VivariumLibera.MOD_ID, exFileHelper);
    }

    private ResourceLocation woodPlumTex(String name) {
        return ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/wood/plum/" + name);
    }

    private ResourceLocation plantTex(String name) {
        return ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plants/" + name);
    }

    @Override
    protected void registerStatesAndModels() {
        // Plum Planks
        Block planks = ModBlocks.PLUM_PLANKS.get();
        simpleBlockWithItem(planks, models().cubeAll("plum_planks", woodPlumTex("plum_planks")));

        // Plum Slab & Stairs
        Block slab = ModBlocks.PLUM_SLAB.get();
        Block stairs = ModBlocks.PLUM_STAIRS.get();
        slabBlock((SlabBlock) slab, ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plum_planks"), woodPlumTex("plum_planks"));
        stairsBlock((StairBlock) stairs, woodPlumTex("plum_planks"));

        // Plum Button & Pressure Plate
        buttonBlock((ButtonBlock) ModBlocks.PLUM_BUTTON.get(), woodPlumTex("plum_planks"));
        pressurePlateBlock((PressurePlateBlock) ModBlocks.PLUM_PRESSURE_PLATE.get(), woodPlumTex("plum_planks"));

        // Plum Fence & Fence Gate
        fenceBlock((FenceBlock) ModBlocks.PLUM_FENCE.get(), woodPlumTex("plum_planks"));
        fenceGateBlock((FenceGateBlock) ModBlocks.PLUM_FENCE_GATE.get(), woodPlumTex("plum_planks"));

        // Plum Trapdoor
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.PLUM_TRAPDOOR.get(), woodPlumTex("plum_trapdoor"), true, "cutout");

        // Plum Logs (use correct axisBlock overload to supply custom side and top textures)
        axisBlock((RotatedPillarBlock) ModBlocks.PLUM_LOG.get(), woodPlumTex("plum_log_side"), woodPlumTex("plum_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_PLUM_LOG.get(), woodPlumTex("plum_stripped_log_side"), woodPlumTex("plum_stripped_log_top"));

        // Plum Leaves (note "pulm" spelling in assets!)
        simpleBlockWithItem(ModBlocks.PLUM_LEAVES.get(),
                models().cubeAll("plum_leaves", woodPlumTex("pulm_leaves")).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.PLUM_LEAVES_1.get(),
                models().cubeAll("plum_leaves_1", woodPlumTex("pulm_leaves_1")).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.PLUM_LEAVES_2.get(),
                models().cubeAll("plum_leaves_2", woodPlumTex("pulm_leaves_2")).renderType("cutout"));

        // Sapling
        simpleBlock(ModBlocks.PLUM_SAPLING.get(),
                models().cross("plum_sapling", woodPlumTex("plum_sapling")).renderType("cutout"));

        // Plants (Cross Model)
        registerPlantBlock(ModBlocks.WORMWOOD, "wormwood");
        registerPlantBlock(ModBlocks.NETTLE, "nettle");
        registerPlantBlock(ModBlocks.HENBANE, "henbane");
        registerPlantBlock(ModBlocks.ST_JOHNS_WORT, "st_johns_wort");
        registerPlantBlock(ModBlocks.DATURA, "datura");
        registerPlantBlock(ModBlocks.CALAMUS, "calamus");
        registerPlantBlock(ModBlocks.CALENDULA, "calendula");
        registerPlantBlock(ModBlocks.COMFREY, "comfrey");
        registerPlantBlock(ModBlocks.EYEBRIGHT, "eyebright");
        registerPlantBlock(ModBlocks.SAGE, "sage");
        registerPlantBlock(ModBlocks.YARROW_WHITE, "yarrow_white");
        registerPlantBlock(ModBlocks.YARROW_RED, "yarrow_red");
        registerPlantBlock(ModBlocks.YARROW_PINK, "yarrow_pink");
        registerPlantBlock(ModBlocks.YARROW_YELLOW, "yarrow_yellow");
        registerPlantBlock(ModBlocks.LYCORIS, "lycoris");
        registerPlantBlock(ModBlocks.CROWS_EYE, "crows_eye");
        registerPlantBlock(ModBlocks.THISTLE, "thistle");
        registerPlantBlock(ModBlocks.MINT, "mint");

        // Regrowing Petals (Meadow Flowers)
        simpleBlock(ModBlocks.WOOD_SORREL_RED.get(),
                models().cross("wood_sorrel_red", plantTex("wood_sorrel_red")).renderType("cutout"));
        simpleBlock(ModBlocks.WOOD_SORREL_YELLOW.get(),
                models().cross("wood_sorrel_yellow", plantTex("wood_sorrel_yellow")).renderType("cutout"));

        // Tall Plants (Double Plant Block)
        registerTallPlantBlock(ModBlocks.FIREWEED, "fireweed_1", "fireweed_2");
        registerTallPlantBlock(ModBlocks.CHICORY, "chicory_bottom", "chicory_top");
        registerTallPlantBlock(ModBlocks.CATTAIL, "cattail_bottom", "cattail_top");

        registerTallPlantBlock(ModBlocks.BELL_FLOWER_BLUE, "bell_flower_blue_bottom", "bell_flower_blue_top");
        registerTallPlantBlock(ModBlocks.BELL_FLOWER_PINK, "bell_flower_pink_bottom", "bell_flower_pink_top");
        registerTallPlantBlock(ModBlocks.BELL_FLOWER_VIOLET, "bell_flower_violet_bottom", "bell_flower_violet_top");
        registerTallPlantBlock(ModBlocks.BELL_FLOWER_WHITE, "bell_flower_white_bottom", "bell_flower_white_top");

        registerTallPlantBlock(ModBlocks.VALERIAN_WHITE, "valerian_white_bot", "valerian__white_top");
        registerTallPlantBlock(ModBlocks.VALERIAN_PINK, "valerian_pink_bot", "valerian__pink_top");
        registerTallPlantBlock(ModBlocks.VALERIAN_RED, "valerian_red_bot", "valerian__red_top");

        registerTallPlantBlock(ModBlocks.BELLADONNA, "belladonna_bottom", "belladonna_top");

        // Mortar
        simpleBlock(ModBlocks.MORTAR.get(), models().getExistingFile(ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/mortar")));
    }

    private void registerPlantBlock(DeferredBlock<? extends Block> block, String textureName) {
        simpleBlock(block.get(), models().cross(block.getId().getPath(), plantTex(textureName)).renderType("cutout"));
    }

    private void registerTallPlantBlock(DeferredBlock<? extends Block> block, String bottomTexture, String topTexture) {
        String name = block.getId().getPath();
        var lowerModel = models().cross(name + "_bottom", plantTex(bottomTexture)).renderType("cutout");
        var upperModel = models().cross(name + "_top", plantTex(topTexture)).renderType("cutout");

        getVariantBuilder(block.get()).forAllStates(state -> {
            boolean isTop = state.getValue(DoubleBlockCombat.HALF) == net.minecraft.world.level.block.state.properties.DoubleBlockHalf.UPPER;
            return net.neoforged.neoforge.client.model.generators.ConfiguredModel.builder()
                    .modelFile(isTop ? upperModel : lowerModel)
                    .build();
        });
    }

    private static class DoubleBlockCombat {
        public static final net.minecraft.world.level.block.state.properties.EnumProperty<net.minecraft.world.level.block.state.properties.DoubleBlockHalf> HALF =
                net.minecraft.world.level.block.state.properties.BlockStateProperties.DOUBLE_BLOCK_HALF;
    }
}
