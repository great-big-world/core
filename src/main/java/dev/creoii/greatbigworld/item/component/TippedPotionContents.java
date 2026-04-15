package dev.creoii.greatbigworld.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jspecify.annotations.Nullable;

public record TippedPotionContents(PotionContents potionContents, int uses) {
    public static final Codec<TippedPotionContents> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                PotionContents.CODEC.fieldOf("potion_contents").forGetter(TippedPotionContents::potionContents),
                Codec.intRange(0, Integer.MAX_VALUE).fieldOf("uses").forGetter(TippedPotionContents::uses)
        ).apply(instance, TippedPotionContents::new);
    });
    public static final StreamCodec<RegistryFriendlyByteBuf, TippedPotionContents> STREAM_CODEC = StreamCodec.composite(PotionContents.STREAM_CODEC, TippedPotionContents::potionContents, ByteBufCodecs.INT, TippedPotionContents::uses, TippedPotionContents::new);

    @Nullable
    public static Holder<Potion> fromEffect(MobEffect mobEffect) {
        if (MobEffects.POISON.value() == mobEffect) return Potions.POISON;
        else if (MobEffects.NIGHT_VISION.value() == mobEffect) return Potions.NIGHT_VISION;
        else if (MobEffects.INVISIBILITY.value() == mobEffect) return Potions.INVISIBILITY;
        else if (MobEffects.JUMP_BOOST.value() == mobEffect) return Potions.LEAPING;
        else if (MobEffects.FIRE_RESISTANCE.value() == mobEffect) return Potions.FIRE_RESISTANCE;
        else if (MobEffects.SPEED.value() == mobEffect) return Potions.SWIFTNESS;
        else if (MobEffects.SLOWNESS.value() == mobEffect) return Potions.SLOWNESS;
        else if (MobEffects.WATER_BREATHING.value() == mobEffect) return Potions.WATER_BREATHING;
        else if (MobEffects.INSTANT_HEALTH.value() == mobEffect) return Potions.HEALING;
        else if (MobEffects.INSTANT_DAMAGE.value() == mobEffect) return Potions.HARMING;
        else if (MobEffects.REGENERATION.value() == mobEffect) return Potions.REGENERATION;
        else if (MobEffects.STRENGTH.value() == mobEffect) return Potions.STRENGTH;
        else if (MobEffects.WEAKNESS.value() == mobEffect) return Potions.WEAKNESS;
        else if (MobEffects.SLOW_FALLING.value() == mobEffect) return Potions.SLOW_FALLING;
        else if (MobEffects.WIND_CHARGED.value() == mobEffect) return Potions.WIND_CHARGED;
        else if (MobEffects.WEAVING.value() == mobEffect) return Potions.WEAVING;
        else if (MobEffects.OOZING.value() == mobEffect) return Potions.OOZING;
        else if (MobEffects.INFESTED.value() == mobEffect) return Potions.INFESTED;
        return null;
    }
}
