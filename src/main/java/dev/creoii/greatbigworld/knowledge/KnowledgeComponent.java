package dev.creoii.greatbigworld.knowledge;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public record KnowledgeComponent(List<Knowledge> knowledge) implements TooltipAppender {
    public static final Codec<KnowledgeComponent> CODEC = Knowledge.CODEC.listOf().xmap(KnowledgeComponent::new, KnowledgeComponent::knowledge);
    public static final PacketCodec<RegistryByteBuf, KnowledgeComponent> PACKET_CODEC = PacketCodec.of(KnowledgeComponent::write, KnowledgeComponent::read);

    public void write(RegistryByteBuf buf) {
        buf.writeInt(knowledge.size());

        knowledge.forEach(knowledge1 -> {
            buf.writeInt(knowledge1.type().ordinal());
            buf.writeIdentifier(knowledge1.data());
        });
    }

    public static KnowledgeComponent read(RegistryByteBuf buf) {
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
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, ComponentsAccess components) {
        knowledge.forEach(knowledge1 -> textConsumer.accept(Text.translatable("knowledge.tooltip", knowledge1.type().getTranslated(), knowledge1.data().getPath()).formatted(Formatting.GRAY)));
    }
}
