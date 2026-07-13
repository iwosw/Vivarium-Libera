package com.iwosw.vivariumlibera.datagen;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import com.iwosw.vivariumlibera.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VivariumLibera.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Flat Items (ingredients, tools, book)
        for (DeferredItem<Item> item : ModItems.CUT_PLANT_ITEMS) {
            simpleItem(item);
        }
        simpleItem(ModItems.KNIFE);
        simpleItem(ModItems.HERBALIST_BOOK);

        // Plant block items (2D in inventory)
        for (var plantItem : ModItems.PLANT_ITEMS) {
            String name = plantItem.getId().getPath();

            // Map the item to its corresponding texture under block/plants/
            ResourceLocation texLoc = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plants/" + name);
            if (name.equals("cattail") || name.equals("fireweed") || name.equals("chicory") ||
                name.startsWith("bell_flower_") || name.startsWith("valerian_") || name.equals("belladonna")) {
                // Tall plants: use bottom texture for the item icon
                if (name.equals("fireweed")) {
                    texLoc = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plants/fireweed_1");
                } else if (name.startsWith("valerian_")) {
                    String color = name.substring("valerian_".length());
                    texLoc = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plants/valerian_" + color + "_bot");
                } else {
                    texLoc = ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plants/" + name + "_bottom");
                }
            }
            singleTexture(name, ResourceLocation.withDefaultNamespace("item/generated"), "layer0", texLoc);
        }

        // Plum Wood block items
        singleTexture("plum_sapling", ResourceLocation.withDefaultNamespace("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/wood/plum/plum_sapling"));

        withExistingParent("plum_slab", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plum_slab"));
        withExistingParent("plum_stairs", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plum_stairs"));

        fenceInventory("plum_fence", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/wood/plum/plum_planks"));
        withExistingParent("plum_fence_gate", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plum_fence_gate"));

        buttonInventory("plum_button", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/wood/plum/plum_planks"));
        withExistingParent("plum_pressure_plate", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/plum_pressure_plate"));

        trapdoorBottom("plum_trapdoor", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/wood/plum/plum_trapdoor"));
        withExistingParent("mortar", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/mortar"));
        withExistingParent("alchemy_table", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "block/alchemy_table"));
    }

    private void simpleItem(DeferredItem<? extends Item> item) {
        String name = item.getId().getPath();
        singleTexture(name, ResourceLocation.withDefaultNamespace("item/generated"),
                "layer0", ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "item/" + name));
    }
}
