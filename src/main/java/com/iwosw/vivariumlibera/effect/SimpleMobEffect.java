package com.iwosw.vivariumlibera.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

/**
 * Plain {@link MobEffect} with a public constructor so it can be instantiated from
 * a registry supplier. Attribute modifiers (if any) are attached via
 * {@link MobEffect#addAttributeModifier} in {@link ModEffects}.
 */
public class SimpleMobEffect extends MobEffect {
    public SimpleMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
}
