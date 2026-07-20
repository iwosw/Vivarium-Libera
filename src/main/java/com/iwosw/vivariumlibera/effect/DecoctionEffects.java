package com.iwosw.vivariumlibera.effect;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * Maps a decoction id (stored in the {@code decoction} data component) to the
 * effects granted when the vial is drunk. Called server-side only.
 */
public final class DecoctionEffects {
    private static final int MIN = 20 * 60;

    private DecoctionEffects() {
    }

    public static void apply(LivingEntity entity, String decoctionId) {
        switch (decoctionId) {
            case "herbal_vision" -> entity.addEffect(new MobEffectInstance(ModEffects.HERBAL_VISION, 4 * MIN));
            case "stone_skin" -> entity.addEffect(new MobEffectInstance(ModEffects.STONE_SKIN, 3 * MIN));
            case "light_step" -> entity.addEffect(new MobEffectInstance(ModEffects.LIGHT_STEP, 3 * MIN));
            case "antidote" -> entity.addEffect(new MobEffectInstance(ModEffects.ANTIDOTE, 2 * MIN));
            case "stupor" -> {
                entity.addEffect(new MobEffectInstance(ModEffects.STUPOR, 30 * 20));
                // Vanilla debuffs drive the actual nausea/blindness; hidden so only STUPOR shows.
                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 30 * 20, 0, true, false, false));
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 20, 0, true, false, false));
            }
            case "fire_blood" -> {
                entity.addEffect(new MobEffectInstance(ModEffects.FIRE_BLOOD, 3 * MIN));
                // Sight in the dark; hidden so only FIRE_BLOOD shows.
                entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 3 * MIN, 0, true, false, false));
            }
            default -> {
                // Unknown decoction id: no effect.
            }
        }
    }
}
