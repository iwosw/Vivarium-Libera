package com.iwosw.vivariumlibera.item;

import com.iwosw.vivariumlibera.effect.DecoctionEffects;
import com.iwosw.vivariumlibera.registry.ModDataComponents;
import com.iwosw.vivariumlibera.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * A brewed decoction in a vial. One item for all recipes: the actual decoction
 * is stored in the {@link ModDataComponents#DECOCTION} data component and drives
 * both the display name and, when drunk, the effects (plan stage 5).
 */
public final class DecoctionItem extends Item {
    private static final int DRINK_DURATION = 32;

    public DecoctionItem(Properties properties) {
        super(properties);
    }

    public static String getDecoctionId(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.DECOCTION.get(), "");
    }

    public static ItemStack create(Item item, String decoctionId) {
        ItemStack stack = new ItemStack(item);
        stack.set(ModDataComponents.DECOCTION.get(), decoctionId);
        return stack;
    }

    @Override
    public Component getName(ItemStack stack) {
        String id = getDecoctionId(stack);
        if (!id.isEmpty()) {
            return Component.translatable("item.vivariumlibera.decoction." + id);
        }
        return super.getName(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return DRINK_DURATION;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            DecoctionEffects.apply(entity, getDecoctionId(stack));
        }

        Player player = entity instanceof Player p ? p : null;
        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
        }

        // Empty vial is returned to the drinker, mirroring vanilla bottle behaviour.
        if (player == null || !player.hasInfiniteMaterials()) {
            ItemStack vial = new ItemStack(ModItems.VIAL.get());
            if (stack.isEmpty()) {
                entity.gameEvent(GameEvent.DRINK);
                return vial;
            }
            if (player != null) {
                player.getInventory().add(vial);
            }
        }

        entity.gameEvent(GameEvent.DRINK);
        return stack;
    }
}
