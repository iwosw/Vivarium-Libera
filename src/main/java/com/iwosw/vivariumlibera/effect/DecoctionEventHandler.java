package com.iwosw.vivariumlibera.effect;

import com.iwosw.vivariumlibera.VivariumLibera;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

/** Server-side hooks for decoction effects that need combat / application events. */
@EventBusSubscriber(modid = VivariumLibera.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public final class DecoctionEventHandler {
    private DecoctionEventHandler() {
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();

        if (entity.hasEffect(ModEffects.FIRE_BLOOD) && event.getSource().is(DamageTypeTags.IS_FIRE)) {
            event.setCanceled(true);
            return;
        }
        if (entity.hasEffect(ModEffects.LIGHT_STEP) && event.getSource().is(DamageTypeTags.IS_FALL)) {
            event.setCanceled(true);
            return;
        }
        if (entity.hasEffect(ModEffects.STONE_SKIN)) {
            event.setAmount(event.getAmount() * 0.7F);
        }
    }

    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (!event.getEntity().hasEffect(ModEffects.ANTIDOTE)) {
            return;
        }
        MobEffect incoming = event.getEffectInstance().getEffect().value();
        if (incoming == MobEffects.POISON.value()
                || incoming == MobEffects.WEAKNESS.value()
                || incoming == MobEffects.WITHER.value()
                || incoming == MobEffects.HUNGER.value()) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }
}
