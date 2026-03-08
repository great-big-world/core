package dev.creoii.greatbigworld.client.toasts;

import dev.creoii.greatbigworld.knowledge.Knowledge;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class KnowledgeToast implements Toast {
    private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("toast/advancement");
    private static final ItemStack BOOK = Items.KNOWLEDGE_BOOK.getDefaultInstance();
    private final Knowledge knowledge;
    private Visibility visibility;

    public KnowledgeToast(Knowledge knowledge) {
        this.knowledge = knowledge;
        visibility = Visibility.HIDE;
    }

    @Override
    public Visibility getWantedVisibility() {
        return visibility;
    }

    @Override
    public void update(ToastManager toastManager, long l) {
        visibility = l >= 5000L * toastManager.getNotificationDisplayTimeMultiplier() ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public @Nullable SoundEvent getSoundEvent() {
        return SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long l) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_SPRITE, 0, 0, width(), height());

        Component displayName = Knowledge.Type.getDisplayName(knowledge.type(), Minecraft.getInstance().level.registryAccess(), knowledge.data());
        List<FormattedCharSequence> list = font.split(displayName, 125);
        if (list.size() == 1) {
            guiGraphics.drawString(font, Component.translatable("knowledge.toast.learned", knowledge.type().getTranslated()), 30, 7, -30465, false);
            guiGraphics.drawString(font, list.getFirst(), 30, 18, -1, false);
        } else {
            if (l < 1500L) {
                int k = Mth.floor(Mth.clamp((float)(1500L - l) / 300.0F, 0.0F, 1.0F) * 255.0F);
                guiGraphics.drawString(font, displayName, 30, 11, ARGB.color(k, -30465), false);
            } else {
                int k = Mth.floor(Mth.clamp((float)(l - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F);
                Objects.requireNonNull(font);
                int m = (height() / 2) - list.size() * 9 / 2;

                for(FormattedCharSequence formattedCharSequence : list) {
                    guiGraphics.drawString(font, formattedCharSequence, 30, m, ARGB.white(k), false);
                    Objects.requireNonNull(font);
                    m += 9;
                }
            }
        }

        guiGraphics.renderFakeItem(BOOK, 8, 8);
    }
}
