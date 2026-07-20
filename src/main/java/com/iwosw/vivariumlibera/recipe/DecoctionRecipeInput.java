package com.iwosw.vivariumlibera.recipe;

import com.iwosw.vivariumlibera.block.entity.AlchemyLiquid;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

/**
 * Snapshot of the alchemy table state used for recipe matching: the three
 * ingredient slots plus the cauldron liquid and the current fire strength.
 */
public record DecoctionRecipeInput(List<ItemStack> ingredients, AlchemyLiquid liquid, boolean strongFire)
        implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return ingredients.get(index);
    }

    @Override
    public int size() {
        return ingredients.size();
    }
}
