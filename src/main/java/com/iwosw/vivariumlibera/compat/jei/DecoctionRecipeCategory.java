package com.iwosw.vivariumlibera.compat.jei;

import com.iwosw.vivariumlibera.block.entity.AlchemyLiquid;
import com.iwosw.vivariumlibera.recipe.DecoctionRecipe;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModRecipes;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class DecoctionRecipeCategory extends AbstractRecipeCategory<RecipeHolder<DecoctionRecipe>> {
    public static final RecipeType<RecipeHolder<DecoctionRecipe>> TYPE =
            RecipeType.createFromVanilla(ModRecipes.DECOCTION_TYPE.get());

    private static final int WIDTH = 154;
    private static final int HEIGHT = 52;

    public DecoctionRecipeCategory(IGuiHelper guiHelper) {
        super(TYPE,
                Component.translatable("gui.vivariumlibera.jei.decoction"),
                guiHelper.createDrawableItemStack(new ItemStack(ModItems.ALCHEMY_TABLE_ITEM.get())),
                WIDTH,
                HEIGHT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<DecoctionRecipe> holder, IFocusGroup focuses) {
        DecoctionRecipe recipe = holder.value();

        builder.addInputSlot(1, 7)
                .addItemStack(jugFor(recipe.liquid()))
                .setStandardSlotBackground();

        List<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < 3; i++) {
            var slot = builder.addInputSlot(25 + i * 18, 7).setStandardSlotBackground();
            if (i < ingredients.size()) {
                slot.addIngredients(ingredients.get(i));
            }
        }

        if (recipe.requiresStrongFire()) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 83, 7)
                    .addItemStack(new ItemStack(ModItems.SAWDUST.get()))
                    .setStandardSlotBackground();
        }

        builder.addInputSlot(107, 27)
                .addItemStack(new ItemStack(ModItems.VIAL.get()))
                .setStandardSlotBackground();

        builder.addOutputSlot(132, 7)
                .addItemStack(recipe.createResult())
                .setOutputSlotBackground();
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<DecoctionRecipe> holder, IFocusGroup focuses) {
        DecoctionRecipe recipe = holder.value();
        builder.addRecipeArrow().setPosition(105, 7);
        int seconds = recipe.cookTime() / 20;
        Component time = Component.translatable("gui.vivariumlibera.jei.decoction.time", seconds);
        builder.addText(time, 100, 10).setPosition(1, 30);
        if (recipe.requiresStrongFire()) {
            builder.addText(Component.translatable("gui.vivariumlibera.jei.decoction.strong_fire"), 152, 10).setPosition(1, 41);
        }
    }

    private static ItemStack jugFor(AlchemyLiquid liquid) {
        return new ItemStack(switch (liquid) {
            case OIL -> ModItems.OIL_JUG_FULL.get();
            case WINE -> ModItems.WINE_JUG_FULL.get();
            default -> ModItems.WATER_JUG_FULL.get();
        });
    }
}
