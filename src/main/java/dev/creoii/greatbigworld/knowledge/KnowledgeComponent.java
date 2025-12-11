package dev.creoii.greatbigworld.knowledge;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;

public record KnowledgeComponent(List<Knowledge> knowledge) implements TooltipProvider {
    public static final Codec<KnowledgeComponent> CODEC = Knowledge.CODEC.listOf().xmap(KnowledgeComponent::new, KnowledgeComponent::knowledge);
    public static final StreamCodec<RegistryFriendlyByteBuf, KnowledgeComponent> PACKET_CODEC = StreamCodec.ofMember(KnowledgeComponent::write, KnowledgeComponent::read);

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeInt(knowledge.size());

        knowledge.forEach(knowledge1 -> {
            buf.writeInt(knowledge1.type().ordinal());
            buf.writeIdentifier(knowledge1.data());
        });
    }

    public static KnowledgeComponent read(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();

        List<Knowledge> knowledges = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            Knowledge.Type type = Knowledge.Type.values()[buf.readInt()];
            Identifier data = buf.readIdentifier();

            knowledges.add(new Knowledge(type, data));
        }

        return new KnowledgeComponent(knowledges);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        knowledge.forEach(knowledge1 -> textConsumer.accept(Component.translatable("knowledge.tooltip", knowledge1.type().getTranslated(), knowledge1.data().getPath()).withStyle(ChatFormatting.GRAY)));
    }
}
