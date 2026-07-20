package com.iwosw.vivariumlibera.effect;

import com.iwosw.vivariumlibera.VivariumLibera;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Custom decoction effects (plan stage 5). */
public final class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, VivariumLibera.MOD_ID);

    /** Highlights the mod's plants through blocks (client render in {@code HerbalVisionRenderer}). */
    public static final Holder<MobEffect> HERBAL_VISION = MOB_EFFECTS.register("herbal_vision",
            () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0x66DD88));

    /** -30% incoming damage (event) and -10% movement speed. */
    public static final Holder<MobEffect> STONE_SKIN = MOB_EFFECTS.register("stone_skin",
            () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0x9A8B7A)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, rl("stone_skin"),
                            -0.10D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    /** No fall damage (event) and a taller step height. */
    public static final Holder<MobEffect> LIGHT_STEP = MOB_EFFECTS.register("light_step",
            () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0xC8E68C)
                    .addAttributeModifier(Attributes.STEP_HEIGHT, rl("light_step"),
                            0.6D, AttributeModifier.Operation.ADD_VALUE));

    /** Cures and grants immunity to poison / weakness / wither / hunger. */
    public static final Holder<MobEffect> ANTIDOTE = MOB_EFFECTS.register("antidote",
            () -> new AntidoteEffect(MobEffectCategory.BENEFICIAL, 0x77CC55));

    /** Debuff carrier: nausea + blindness are applied alongside it on drink. */
    public static final Holder<MobEffect> STUPOR = MOB_EFFECTS.register("stupor",
            () -> new SimpleMobEffect(MobEffectCategory.HARMFUL, 0x5A4A7A));

    /** Fire immunity (event) plus night vision applied alongside it on drink. */
    public static final Holder<MobEffect> FIRE_BLOOD = MOB_EFFECTS.register("fire_blood",
            () -> new SimpleMobEffect(MobEffectCategory.BENEFICIAL, 0xE0662B));

    private ModEffects() {
    }

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

    private static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, path);
    }
}
