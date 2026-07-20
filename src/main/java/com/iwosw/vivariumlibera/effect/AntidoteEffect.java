package com.iwosw.vivariumlibera.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * Antidote: cures poison / weakness / wither / hunger the moment it is applied.
 * Ongoing immunity while the effect lasts is enforced in
 * {@link DecoctionEventHandler#onEffectApplicable}.
 */
public final class AntidoteEffect extends SimpleMobEffect {
    public AntidoteEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onEffectStarted(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return;
        }
        entity.removeEffect(MobEffects.POISON);
        entity.removeEffect(MobEffects.WEAKNESS);
        entity.removeEffect(MobEffects.WITHER);
        entity.removeEffect(MobEffects.HUNGER);
    }
}
