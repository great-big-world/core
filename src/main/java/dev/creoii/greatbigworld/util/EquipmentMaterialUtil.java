package dev.creoii.greatbigworld.util;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;

public final class EquipmentMaterialUtil {
    public static final Map<ArmorMaterial, Entry> ARMOR_ENTRIES = new HashMap<>();
    public static final Map<ToolMaterial, Entry> TOOL_ENTRIES = new HashMap<>();
    public static final StreamCodec<RegistryFriendlyByteBuf, String> PACKET_CODEC = StreamCodec.ofMember((value, buf) -> buf.writeUtf(value), FriendlyByteBuf::readUtf);
    public static final ToolMaterial MACE = new ToolMaterial(null, 0, 0f, 0f, 0, null);
    public static final ToolMaterial TRIDENT = new ToolMaterial(null, 0, 0f, 0f, 0, null);
    public static final ToolMaterial SHIELD = new ToolMaterial(null, 0, 0f, 0f, 0, null);

    @Nullable
    public static ToolMaterial getToolMaterial(String id) {
        return switch (id) {
            case "wood" -> ToolMaterial.WOOD;
            case "stone" -> ToolMaterial.STONE;
            case "iron" -> ToolMaterial.IRON;
            case "gold" -> ToolMaterial.GOLD;
            case "diamond" -> ToolMaterial.DIAMOND;
            case "netherite" -> ToolMaterial.NETHERITE;
            case "copper" -> ToolMaterial.COPPER;
            case "mace" -> MACE;
            case "trident" -> TRIDENT;
            case "shield" -> SHIELD;
            default -> null;
        };
    }

    @Nullable
    public static ArmorMaterial getArmorMaterial(String id) {
        return switch (id) {
            case "leather" -> ArmorMaterials.LEATHER;
            case "chain" -> ArmorMaterials.CHAINMAIL;
            case "iron" -> ArmorMaterials.IRON;
            case "gold" -> ArmorMaterials.GOLD;
            case "diamond" -> ArmorMaterials.DIAMOND;
            case "netherite" -> ArmorMaterials.NETHERITE;
            case "copper" -> ArmorMaterials.COPPER;
            case "turtle_scute" -> ArmorMaterials.TURTLE_SCUTE;
            case "armadillo_scute" -> ArmorMaterials.ARMADILLO_SCUTE;
            default -> null;
        };
    }

    public static void register(ArmorMaterial armorMaterial, int maxEnchantmentLevel, int totalEnchantmentLevelCap, int maxUpgrades) {
        ARMOR_ENTRIES.put(armorMaterial, new Entry(maxEnchantmentLevel, totalEnchantmentLevelCap, maxUpgrades));
    }

    public static void register(ToolMaterial toolMaterial, int maxEnchantmentLevel, int totalEnchantmentLevelCap, int maxUpgrades) {
        TOOL_ENTRIES.put(toolMaterial, new Entry(maxEnchantmentLevel, totalEnchantmentLevelCap, maxUpgrades));
    }

    public record Entry(int maxEnchantmentLevel, int totalEnchantmentLevelCap, int maxUpgrades) {
    }
}
