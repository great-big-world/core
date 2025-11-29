package dev.creoii.greatbigworld.knowledge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;

import java.util.function.BiFunction;
import java.util.function.Function;

public record Knowledge(Type type, Identifier data) {
    public static final Codec<Knowledge> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(
                Type.CODEC.fieldOf("type").forGetter(Knowledge::type),
                Identifier.CODEC.fieldOf("data").forGetter(Knowledge::data)
        ).apply(instance, Knowledge::new);
    });

    public Knowledge(Type type, RegistryKey<?> data) {
        this(type, data.getValue());
    }

    public enum Type implements StringIdentifiable {
        ENCHANTMENT("knowledge.type.enchantment", true, id -> Identifier.ofVanilla("textures/item/book.png"), (registryManager, id) -> registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).get(id).description()),
        ARMOR_TRIM("knowledge.type.armor_trim", false, id -> {
            Item armorTrim = KnowledgeUtil.getStackFromArmorTrimPattern(RegistryKey.of(RegistryKeys.TRIM_PATTERN, id));
            Identifier itemId = Registries.ITEM.getId(armorTrim);
            return Identifier.of(itemId.getNamespace(), "textures/item/" + itemId.getPath() + ".png");
        }, (registryManager, id) -> registryManager.getOrThrow(RegistryKeys.TRIM_PATTERN).get(id).description()),
        POTTERY_SHERD("knowledge.type.pottery_sherd", false, id -> Identifier.of(id.getNamespace(), "textures/item/" + id.getPath() + ".png"), (registryManager, id) -> {
            Item bannerPattern = registryManager.getOrThrow(RegistryKeys.ITEM).get(id);
            RegistryKey<DecoratedPotPattern> key = KnowledgeUtil.getDecoratedPotPatternFromItem(bannerPattern);
            if (key != null) {
                DecoratedPotPattern pattern = registryManager.getOrThrow(RegistryKeys.DECORATED_POT_PATTERN).get(key);
                return Text.translatable(pattern.assetId().getNamespace() + ".decorated_pot_pattern." + pattern.assetId().getPath());
            }
            return ScreenTexts.EMPTY;
        }),
        BANNER_PATTERN("knowledge.type.banner_pattern", false, id -> {
            Item bannerPattern = KnowledgeUtil.getItemFromBannerPattern(RegistryKey.of(RegistryKeys.BANNER_PATTERN, id));
            Identifier itemId = Registries.ITEM.getId(bannerPattern);
            return Identifier.of(itemId.getNamespace(), "textures/item/" + itemId.getPath() + ".png");
        }, (registryManager, id) -> Text.translatable(registryManager.getOrThrow(RegistryKeys.BANNER_PATTERN).get(id).translationKey()));

        public static final Codec<Knowledge.Type> CODEC = StringIdentifiable.createCodec(Knowledge.Type::values);
        private final Text translated;
        private final Text translatedPlural;
        private final boolean glint;
        private final Function<Identifier, Identifier> spriteIdentifier;
        private final BiFunction<DynamicRegistryManager, Identifier, Text> displayName;

        Type(String translationKey, boolean glint, Function<Identifier, Identifier> spriteIdentifier, BiFunction<DynamicRegistryManager, Identifier, Text> displayName) {
            this.translated = Text.translatable(translationKey);
            this.translatedPlural = Text.translatable(translationKey + ".plural");
            this.glint = glint;
            this.spriteIdentifier = spriteIdentifier;
            this.displayName = displayName;
        }

        public Text getTranslated() {
            return translated;
        }

        public Text getTranslatedPlural() {
            return translatedPlural;
        }

        public boolean hasGlint() {
            return glint;
        }

        public Identifier getSpriteIdentifier(Identifier id) {
            return spriteIdentifier.apply(id);
        }

        public Text getDisplayName(DynamicRegistryManager registryManager, Identifier id) {
            return displayName.apply(registryManager, id);
        }

        @Override
        public String asString() {
            return name().toLowerCase();
        }
    }
}
