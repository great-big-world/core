package dev.creoii.greatbigworld.knowledge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

public record Knowledge(Type type, Identifier data) {
    public static final Codec<Knowledge> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Type.CODEC.fieldOf("type").forGetter(Knowledge::type),
                Identifier.CODEC.fieldOf("data").forGetter(Knowledge::data)
        ).apply(instance, Knowledge::new);
    });

    public Knowledge(Type type, ResourceKey<?> data) {
        this(type, data.identifier());
    }

    public enum Type implements StringRepresentable {
        ENCHANTMENT("knowledge.type.enchantment", true, id -> Identifier.withDefaultNamespace("textures/item/book.png"), (registryManager, id) -> registryManager.lookupOrThrow(Registries.ENCHANTMENT).getValue(id).description()),
        ARMOR_TRIM("knowledge.type.armor_trim", false, id -> {
            Item armorTrim = KnowledgeUtil.getStackFromArmorTrimPattern(ResourceKey.create(Registries.TRIM_PATTERN, id));
            Identifier itemId = BuiltInRegistries.ITEM.getKey(armorTrim);
            return Identifier.fromNamespaceAndPath(itemId.getNamespace(), "textures/item/" + itemId.getPath() + ".png");
        }, (registryManager, id) -> registryManager.lookupOrThrow(Registries.TRIM_PATTERN).getValue(id).description()),
        POTTERY_SHERD("knowledge.type.pottery_sherd", false, id -> Identifier.fromNamespaceAndPath(id.getNamespace(), "textures/item/" + id.getPath() + ".png"), (registryManager, id) -> {
            Item bannerPattern = registryManager.lookupOrThrow(Registries.ITEM).getValue(id);
            ResourceKey<DecoratedPotPattern> key = KnowledgeUtil.getDecoratedPotPatternFromItem(bannerPattern);
            if (key != null) {
                DecoratedPotPattern pattern = registryManager.lookupOrThrow(Registries.DECORATED_POT_PATTERN).getValue(key);
                return Component.translatable(pattern.assetId().getNamespace() + ".decorated_pot_pattern." + pattern.assetId().getPath());
            }
            return CommonComponents.EMPTY;
        }),
        BANNER_PATTERN("knowledge.type.banner_pattern", false, id -> {
            Item bannerPattern = KnowledgeUtil.getItemFromBannerPattern(ResourceKey.create(Registries.BANNER_PATTERN, id));
            Identifier itemId = BuiltInRegistries.ITEM.getKey(bannerPattern);
            return Identifier.fromNamespaceAndPath(itemId.getNamespace(), "textures/item/" + itemId.getPath() + ".png");
        }, (registryManager, id) -> Component.translatable(registryManager.lookupOrThrow(Registries.BANNER_PATTERN).getValue(id).translationKey()));

        public static final Codec<Knowledge.Type> CODEC = StringRepresentable.fromEnum(Knowledge.Type::values);
        private final Component translated;
        private final Component translatedPlural;
        private final boolean glint;
        private final Function<Identifier, Identifier> spriteIdentifier;
        private final BiFunction<RegistryAccess, Identifier, Component> displayName;

        Type(String translationKey, boolean glint, Function<Identifier, Identifier> spriteIdentifier, BiFunction<RegistryAccess, Identifier, Component> displayName) {
            this.translated = Component.translatable(translationKey);
            this.translatedPlural = Component.translatable(translationKey + ".plural");
            this.glint = glint;
            this.spriteIdentifier = spriteIdentifier;
            this.displayName = displayName;
        }

        public Component getTranslated() {
            return translated;
        }

        public Component getTranslatedPlural() {
            return translatedPlural;
        }

        public boolean hasGlint() {
            return glint;
        }

        public Identifier getSpriteIdentifier(Identifier id) {
            return spriteIdentifier.apply(id);
        }

        public Component getDisplayName(RegistryAccess registryManager, Identifier id) {
            return displayName.apply(registryManager, id);
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}
