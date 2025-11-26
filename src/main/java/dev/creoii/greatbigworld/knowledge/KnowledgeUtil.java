package dev.creoii.greatbigworld.knowledge;

import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.equipment.trim.ArmorTrimPattern;
import net.minecraft.item.equipment.trim.ArmorTrimPatterns;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BannerPatternTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

public final class KnowledgeUtil {
    @Nullable
    public static RegistryKey<ArmorTrimPattern> getArmorTrimPatternFromStack(ItemStack stack) {
        RegistryKey<ArmorTrimPattern> armorTrimPattern = null;
        if (stack.isOf(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.SENTRY;
        else if (stack.isOf(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.DUNE;
        else if (stack.isOf(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.COAST;
        else if (stack.isOf(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.WILD;
        else if (stack.isOf(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.WARD;
        else if (stack.isOf(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.EYE;
        else if (stack.isOf(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.VEX;
        else if (stack.isOf(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.TIDE;
        else if (stack.isOf(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.SNOUT;
        else if (stack.isOf(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.RIB;
        else if (stack.isOf(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.SPIRE;
        else if (stack.isOf(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.WAYFINDER;
        else if (stack.isOf(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.SHAPER;
        else if (stack.isOf(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.SILENCE;
        else if (stack.isOf(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.RAISER;
        else if (stack.isOf(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.HOST;
        else if (stack.isOf(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.FLOW;
        else if (stack.isOf(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = ArmorTrimPatterns.BOLT;
        return armorTrimPattern;
    }

    @Nullable
    public static RegistryKey<BannerPattern> getBannerPatternFromTag(TagKey<BannerPattern> bannerPatternTagKey) {
        RegistryKey<BannerPattern> pattern = null;
        if (bannerPatternTagKey == BannerPatternTags.FLOWER_PATTERN_ITEM) pattern = BannerPatterns.FLOWER;
        else if (bannerPatternTagKey == BannerPatternTags.CREEPER_PATTERN_ITEM) pattern = BannerPatterns.CREEPER;
        else if (bannerPatternTagKey == BannerPatternTags.SKULL_PATTERN_ITEM) pattern = BannerPatterns.SKULL;
        else if (bannerPatternTagKey == BannerPatternTags.MOJANG_PATTERN_ITEM) pattern = BannerPatterns.MOJANG;
        else if (bannerPatternTagKey == BannerPatternTags.GLOBE_PATTERN_ITEM) pattern = BannerPatterns.GLOBE;
        else if (bannerPatternTagKey == BannerPatternTags.PIGLIN_PATTERN_ITEM) pattern = BannerPatterns.PIGLIN;
        else if (bannerPatternTagKey == BannerPatternTags.FLOW_PATTERN_ITEM) pattern = BannerPatterns.FLOW;
        else if (bannerPatternTagKey == BannerPatternTags.GUSTER_PATTERN_ITEM) pattern = BannerPatterns.GUSTER;
        else if (bannerPatternTagKey == BannerPatternTags.FIELD_MASONED_PATTERN_ITEM) pattern = BannerPatterns.BRICKS;
        else if (bannerPatternTagKey == BannerPatternTags.BORDURE_INDENTED_PATTERN_ITEM) pattern = BannerPatterns.CURLY_BORDER;
        return pattern;
    }

    @Nullable
    public static Item getStackFromArmorTrimPattern(RegistryKey<ArmorTrimPattern> pattern) {
        if (pattern == ArmorTrimPatterns.SENTRY) return Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.DUNE) return Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.COAST) return Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.WILD) return Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.WARD) return Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.EYE) return Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.VEX) return Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.TIDE) return Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.SNOUT) return Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.RIB) return Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.SPIRE) return Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.WAYFINDER) return Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.SHAPER) return Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.SILENCE) return Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.RAISER) return Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.HOST) return Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.FLOW) return Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == ArmorTrimPatterns.BOLT) return Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE;
        return null;
    }

    @Nullable
    public static Item getItemFromBannerPattern(RegistryKey<BannerPattern> pattern) {
        if (pattern == BannerPatterns.FLOWER) return Items.FLOWER_BANNER_PATTERN;
        else if (pattern == BannerPatterns.CREEPER) return Items.CREEPER_BANNER_PATTERN;
        else if (pattern == BannerPatterns.SKULL) return Items.SKULL_BANNER_PATTERN;
        else if (pattern == BannerPatterns.MOJANG) return Items.MOJANG_BANNER_PATTERN;
        else if (pattern == BannerPatterns.GLOBE) return Items.GLOBE_BANNER_PATTERN;
        else if (pattern == BannerPatterns.PIGLIN) return Items.PIGLIN_BANNER_PATTERN;
        else if (pattern == BannerPatterns.FLOW) return Items.FLOW_BANNER_PATTERN;
        else if (pattern == BannerPatterns.GUSTER) return Items.GUSTER_BANNER_PATTERN;
        else if (pattern == BannerPatterns.BRICKS) return Items.FIELD_MASONED_BANNER_PATTERN;
        else if (pattern == BannerPatterns.CURLY_BORDER) return Items.BORDURE_INDENTED_BANNER_PATTERN;
        return null;
    }

    @Nullable
    public static RegistryKey<DecoratedPotPattern> getDecoratedPotPatternFromItem(Item item) {
        if (item == Items.ANGLER_POTTERY_SHERD) return DecoratedPotPatterns.SHEAF;
        else if (item == Items.ARCHER_POTTERY_SHERD) return DecoratedPotPatterns.ARCHER;
        else if (item == Items.ARMS_UP_POTTERY_SHERD) return DecoratedPotPatterns.ARMS_UP;
        else if (item == Items.BLADE_POTTERY_SHERD) return DecoratedPotPatterns.BLADE;
        else if (item == Items.BREWER_POTTERY_SHERD) return DecoratedPotPatterns.BREWER;
        else if (item == Items.BURN_POTTERY_SHERD) return DecoratedPotPatterns.BURN;
        else if (item == Items.DANGER_POTTERY_SHERD) return DecoratedPotPatterns.DANGER;
        else if (item == Items.EXPLORER_POTTERY_SHERD) return DecoratedPotPatterns.EXPLORER;
        else if (item == Items.FLOW_BANNER_PATTERN) return DecoratedPotPatterns.FLOW;
        else if (item == Items.FRIEND_POTTERY_SHERD) return DecoratedPotPatterns.FRIEND;
        else if (item == Items.GUSTER_POTTERY_SHERD) return DecoratedPotPatterns.GUSTER;
        else if (item == Items.HEART_POTTERY_SHERD) return DecoratedPotPatterns.HEART;
        else if (item == Items.HEARTBREAK_POTTERY_SHERD) return DecoratedPotPatterns.HEARTBREAK;
        else if (item == Items.HOWL_POTTERY_SHERD) return DecoratedPotPatterns.HOWL;
        else if (item == Items.MINER_POTTERY_SHERD) return DecoratedPotPatterns.MINER;
        else if (item == Items.MOURNER_POTTERY_SHERD) return DecoratedPotPatterns.MOURNER;
        else if (item == Items.PLENTY_POTTERY_SHERD) return DecoratedPotPatterns.PLENTY;
        else if (item == Items.PRIZE_POTTERY_SHERD) return DecoratedPotPatterns.PRIZE;
        else if (item == Items.SCRAPE_POTTERY_SHERD) return DecoratedPotPatterns.SCRAPE;
        else if (item == Items.SHEAF_POTTERY_SHERD) return DecoratedPotPatterns.SHEAF;
        else if (item == Items.SHELTER_POTTERY_SHERD) return DecoratedPotPatterns.SHELTER;
        else if (item == Items.SKULL_POTTERY_SHERD) return DecoratedPotPatterns.SKULL;
        else if (item == Items.SNORT_POTTERY_SHERD) return DecoratedPotPatterns.SNORT;
        return null;
    }
}
