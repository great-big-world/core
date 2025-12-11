package dev.creoii.greatbigworld.knowledge;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.TrimPattern;
import net.minecraft.world.item.equipment.trim.TrimPatterns;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import org.jetbrains.annotations.Nullable;

public final class KnowledgeUtil {
    @Nullable
    public static ResourceKey<TrimPattern> getArmorTrimPatternFromStack(ItemStack stack) {
        ResourceKey<TrimPattern> armorTrimPattern = null;
        if (stack.is(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.SENTRY;
        else if (stack.is(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.DUNE;
        else if (stack.is(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.COAST;
        else if (stack.is(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.WILD;
        else if (stack.is(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.WARD;
        else if (stack.is(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.EYE;
        else if (stack.is(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.VEX;
        else if (stack.is(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.TIDE;
        else if (stack.is(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.SNOUT;
        else if (stack.is(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.RIB;
        else if (stack.is(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.SPIRE;
        else if (stack.is(Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.WAYFINDER;
        else if (stack.is(Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.SHAPER;
        else if (stack.is(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.SILENCE;
        else if (stack.is(Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.RAISER;
        else if (stack.is(Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.HOST;
        else if (stack.is(Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.FLOW;
        else if (stack.is(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE)) armorTrimPattern = TrimPatterns.BOLT;
        return armorTrimPattern;
    }

    @Nullable
    public static ResourceKey<BannerPattern> getBannerPatternFromTag(TagKey<BannerPattern> bannerPatternTagKey) {
        ResourceKey<BannerPattern> pattern = null;
        if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_FLOWER) pattern = BannerPatterns.FLOWER;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_CREEPER) pattern = BannerPatterns.CREEPER;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_SKULL) pattern = BannerPatterns.SKULL;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_MOJANG) pattern = BannerPatterns.MOJANG;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_GLOBE) pattern = BannerPatterns.GLOBE;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_PIGLIN) pattern = BannerPatterns.PIGLIN;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_FLOW) pattern = BannerPatterns.FLOW;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_GUSTER) pattern = BannerPatterns.GUSTER;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_FIELD_MASONED) pattern = BannerPatterns.BRICKS;
        else if (bannerPatternTagKey == BannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED) pattern = BannerPatterns.CURLY_BORDER;
        return pattern;
    }

    @Nullable
    public static Item getStackFromArmorTrimPattern(ResourceKey<TrimPattern> pattern) {
        if (pattern == TrimPatterns.SENTRY) return Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.DUNE) return Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.COAST) return Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.WILD) return Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.WARD) return Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.EYE) return Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.VEX) return Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.TIDE) return Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.SNOUT) return Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.RIB) return Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.SPIRE) return Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.WAYFINDER) return Items.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.SHAPER) return Items.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.SILENCE) return Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.RAISER) return Items.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.HOST) return Items.HOST_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.FLOW) return Items.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE;
        else if (pattern == TrimPatterns.BOLT) return Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE;
        return null;
    }

    @Nullable
    public static Item getItemFromBannerPattern(ResourceKey<BannerPattern> pattern) {
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
    public static ResourceKey<DecoratedPotPattern> getDecoratedPotPatternFromItem(Item item) {
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
