package com.iwosw.vivariumlibera.datagen;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import com.iwosw.vivariumlibera.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // Planks from logs
        planksFromLog(recipeOutput, ModBlocks.PLUM_PLANKS.get(), ModBlocks.PLUM_LOG.get(), 4);
        planksFromLog(recipeOutput, ModBlocks.PLUM_PLANKS.get(), ModBlocks.STRIPPED_PLUM_LOG.get(), 4);

        // Wood recipes (Slabs, Stairs, Button, Pressure Plate, Fence, Fence Gate, Trapdoor)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PLUM_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', ModBlocks.PLUM_PLANKS.get())
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.PLUM_SLAB.get(), 6)
                .pattern("###")
                .define('#', ModBlocks.PLUM_PLANKS.get())
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PLUM_PRESSURE_PLATE.get())
                .pattern("##")
                .define('#', ModBlocks.PLUM_PLANKS.get())
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.PLUM_BUTTON.get())
                .requires(ModBlocks.PLUM_PLANKS.get())
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PLUM_FENCE.get(), 3)
                .pattern("#W#")
                .pattern("#W#")
                .define('#', ModBlocks.PLUM_PLANKS.get())
                .define('W', Items.STICK)
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PLUM_FENCE_GATE.get())
                .pattern("W#W")
                .pattern("W#W")
                .define('#', ModBlocks.PLUM_PLANKS.get())
                .define('W', Items.STICK)
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.PLUM_TRAPDOOR.get(), 2)
                .pattern("###")
                .pattern("###")
                .define('#', ModBlocks.PLUM_PLANKS.get())
                .unlockedBy("has_planks", has(ModBlocks.PLUM_PLANKS.get()))
                .save(recipeOutput);

        // Mortar recipe (3 Stone)
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.MORTAR.get())
                .pattern("# #")
                .pattern(" # ")
                .define('#', Items.STONE)
                .unlockedBy("has_stone", has(Items.STONE))
                .save(recipeOutput);

        // Knife recipe (1 Iron Ingot + 1 Stick)
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.KNIFE.get())
                .pattern("I")
                .pattern("S")
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(recipeOutput);

        // Simple cut recipe examples:
        registerCuttingRecipe(recipeOutput, ModItems.WORMWOOD_CUT.get(), ModItems.WORMWOOD_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.NETTLE_CUT.get(), ModItems.NETTLE_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.HENBANE_CUT.get(), ModItems.HENBANE_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.ST_JOHNS_WORT_CUT.get(), ModItems.ST_JOHNS_WORT_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.DATURA_CUT.get(), ModItems.DATURA_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.FIREWEED_CUT.get(), ModItems.FIREWEED_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.CHICORY_CUT.get(), ModItems.CHICORY_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.COMFREY_CUT.get(), ModItems.COMFREY_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.EYEBRIGHT_CUT.get(), ModItems.EYEBRIGHT_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.SAGE_CUT.get(), ModItems.SAGE_ITEM.get());
        registerCuttingRecipe(recipeOutput, ModItems.CALENDULA_CUT.get(), ModItems.CALENDULA_ITEM.get());
    }

    private void planksFromLog(RecipeOutput recipeOutput, net.minecraft.world.level.block.Block planks, net.minecraft.world.level.block.Block log, int count) {
        String logName = BuiltInRegistries.BLOCK.getKey(log).getPath();
        String planksName = BuiltInRegistries.BLOCK.getKey(planks).getPath();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, planks, count)
                .requires(log)
                .unlockedBy("has_log", has(log))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, planksName + "_from_" + logName));
    }

    private void registerCuttingRecipe(RecipeOutput recipeOutput, net.minecraft.world.item.Item result, net.minecraft.world.item.Item input) {
        String resultName = BuiltInRegistries.ITEM.getKey(result).getPath();
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, result, 2)
                .requires(input)
                .requires(ModItems.KNIFE.get())
                .unlockedBy("has_knife", has(ModItems.KNIFE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, resultName + "_from_cutting"));
    }
}
