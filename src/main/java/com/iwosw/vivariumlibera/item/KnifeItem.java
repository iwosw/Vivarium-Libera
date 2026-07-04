package com.iwosw.vivariumlibera.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;

public class KnifeItem extends Item {
    private static final ItemAbility KNIFE_DIG = ItemAbility.get("knife_dig");

    public KnifeItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return itemAbility == KNIFE_DIG || super.canPerformAction(stack, itemAbility);
    }
}
