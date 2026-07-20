package com.iwosw.vivariumlibera.compat.jei;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.recipe.DecoctionRecipe;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModRecipes;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

@JeiPlugin
public class JEIVivariumPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DecoctionRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        List<RecipeHolder<DecoctionRecipe>> recipes = level.getRecipeManager().getAllRecipesFor(ModRecipes.DECOCTION_TYPE.get());
        registration.addRecipes(DecoctionRecipeCategory.TYPE, recipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Here we will register click areas and transfer handlers for the alchemy GUI
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModItems.ALCHEMY_TABLE_ITEM.get()), DecoctionRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModItems.ALCHEMY_CAULDRON.get()), DecoctionRecipeCategory.TYPE);
    }
}
