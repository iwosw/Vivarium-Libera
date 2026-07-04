package com.iwosw.vivariumlibera.compat.jei;

import com.iwosw.vivariumlibera.VivariumLibera;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class JEIVivariumPlugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_ID = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        // Here we will register the Alchemy recipe category when ready
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // Here we will load and register recipes
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Here we will register click areas and transfer handlers for the alchemy GUI
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Here we will register the Alchemy Table block as a catalyst for alchemy recipes
    }
}
