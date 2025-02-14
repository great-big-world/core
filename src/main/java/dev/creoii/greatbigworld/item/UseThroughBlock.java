package dev.creoii.greatbigworld.item;

import dev.creoii.greatbigworld.GreatBigWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public interface UseThroughBlock {
    default void onAttackThroughBlock(PlayerEntity player, ItemStack stack, Entity target) {}

    default boolean canAttackThroughBlock(PlayerEntity player, ItemStack stack, Entity target) {
        return false;
    }

    record AttackThroughBlock(int entityId) implements CustomPayload {
        public static final CustomPayload.Id<AttackThroughBlock> PACKET_ID = new CustomPayload.Id<>(Identifier.of(GreatBigWorld.NAMESPACE, "attack_through_block"));
        public static final PacketCodec<RegistryByteBuf, AttackThroughBlock> PACKET_CODEC = PacketCodec.of(AttackThroughBlock::write, AttackThroughBlock::new);

        public AttackThroughBlock(RegistryByteBuf buf) {
            this(buf.readVarInt());
        }

        public void write(RegistryByteBuf buf) {
            buf.writeVarInt(entityId);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return PACKET_ID;
        }
    }
}
