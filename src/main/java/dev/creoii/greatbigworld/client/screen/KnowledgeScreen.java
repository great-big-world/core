package dev.creoii.greatbigworld.client.screen;

import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.util.network.RequestKnowledgeC2S;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class KnowledgeScreen extends Screen {
    private static final Text TITLE_TEXT = Text.translatable("gui.knowledge");
    protected final Screen parent;
    final ThreePartsLayoutWidget layout = new ThreePartsLayoutWidget(this);
    private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
    @Nullable
    private TabNavigationWidget tabNavigationWidget;

    public KnowledgeScreen(Screen parent) {
        super(TITLE_TEXT);
        this.parent = parent;
    }

    public Screen getParent() {
        return parent;
    }

    protected void init() {
        tabNavigationWidget = TabNavigationWidget.builder(tabManager, width).tabs(Arrays.stream(Knowledge.Type.values()).map(this::getFromType).toArray(Tab[]::new)).build();
        addDrawableChild(tabNavigationWidget);
        layout.addFooter(ButtonWidget.builder(ScreenTexts.DONE, button -> close()).width(200).build());

        if (!tabNavigationWidget.getTabs().isEmpty()) {
            for (int i = 0; i < tabNavigationWidget.getTabs().size(); ++i) {
                tabNavigationWidget.setTabActive(i, true);
            }
            layout.forEachChild((child) -> {
                child.setNavigationOrder(1);
                addDrawableChild(child);
            });
            tabNavigationWidget.selectTab(0, false);
        }
        refreshWidgetPositions();
        ClientPlayNetworking.send(new RequestKnowledgeC2S());
    }

    protected void refreshWidgetPositions() {
        if (tabNavigationWidget != null) {
            tabNavigationWidget.setWidth(width);
            tabNavigationWidget.init();
            int i = tabNavigationWidget.getNavigationFocus().getBottom();
            ScreenRect screenRect = new ScreenRect(0, i, width, height - layout.getFooterHeight() - i);
            tabNavigationWidget.getTabs().forEach(tab -> tab.forEachChild(child -> child.setHeight(screenRect.height())));
            tabManager.setTabArea(screenRect);
            layout.setHeaderHeight(i);
            layout.refreshPositions();
        }
    }

    public boolean keyPressed(KeyInput input) {
        return tabNavigationWidget != null && tabNavigationWidget.keyPressed(input) || super.keyPressed(input);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawTexture(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR_TEXTURE, 0, height - layout.getFooterHeight(), 0.0F, 0.0F, width, 2, 32, 2);
    }

    protected void renderDarkening(DrawContext context) {
        context.drawTexture(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND_TEXTURE, 0, 0, 0.0F, 0.0F, width, layout.getHeaderHeight(), 16, 16);
        renderDarkening(context, 0, layout.getHeaderHeight(), width, height);
    }

    public void close() {
        client.setScreen(parent);
    }

    private Tab getFromType(Knowledge.Type type) {
        return new KnowledgeTab(type, type.getTranslated(), new KnowledgeListWidget(client, type));
    }

    @Environment(EnvType.CLIENT)
    public class KnowledgeTab extends GridScreenTab {
        private final Knowledge.Type type;
        protected final EntryListWidget<?> widget;

        public KnowledgeTab(Knowledge.Type type, Text title, final EntryListWidget<?> widget) {
            super(title);
            this.type = type;
            this.widget = widget;
        }

        public Knowledge.Type getType() {
            return type;
        }

        public void refreshGrid(ScreenRect tabArea) {
            widget.position(KnowledgeScreen.this.width, KnowledgeScreen.this.layout.getContentHeight(), KnowledgeScreen.this.layout.getHeaderHeight());
            new ArrayList<>(KnowledgeScreen.this.children()).forEach(element -> {
                if (element instanceof KnowledgeListWidget)
                    KnowledgeScreen.this.remove(element);
            });
            KnowledgeScreen.this.addDrawableChild(widget);
            super.refreshGrid(tabArea);
        }
    }

    @Environment(EnvType.CLIENT)
    class KnowledgeListWidget extends EntryListWidget<KnowledgeListWidget.Entry> {
        private final Knowledge.Type knowledgeType;

        public KnowledgeListWidget(final MinecraftClient client, Knowledge.Type type) {
            super(client, KnowledgeScreen.this.width, KnowledgeScreen.this.layout.getContentHeight(), 33, 14);
            this.knowledgeType = type;

            ObjectArrayList<Knowledge> objectArrayList = new ObjectArrayList<>(GreatBigWorldClient.getKnowledge().getOrDefault(type, new HashSet<>()).iterator());
            if (!objectArrayList.isEmpty()) {
                objectArrayList.sort(Comparator.comparing(knowledge -> I18n.translate(knowledge.data().toString())));

                for (Knowledge knowledge : objectArrayList) {
                    addEntry(new Entry(knowledge.data()));
                }
            }
        }

        public int getRowWidth() {
            return 280;
        }

        protected void drawMenuListBackground(DrawContext context) {
        }

        protected void drawHeaderAndFooterSeparators(DrawContext context) {
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }

        @Override
        public @Nullable Entry getFocused() {
            return super.getFocused();
        }

        @Environment(EnvType.CLIENT)
        class Entry extends EntryListWidget.Entry<Entry> {
            private final Identifier id;

            Entry(Identifier id) {
                this.id = id;
            }

            public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
                Objects.requireNonNull(KnowledgeScreen.this.textRenderer);
                int i = getContentMiddleY() - 9 / 2;
                int j = KnowledgeListWidget.this.children().indexOf(this);
                int k = j % 2 == 0 ? -1 : -4539718;
                context.drawTextWithShadow(KnowledgeScreen.this.textRenderer, knowledgeType.getDisplayName(client.world.getRegistryManager(), id), getContentX() + 2, i, k);

                context.drawTexture(RenderPipelines.GUI_TEXTURED, knowledgeType.getSpriteIdentifier(id), getContentRightEnd() - 16 - 4, i, 0f, 0f, 16, 16, 16, 16);
                if (knowledgeType.hasGlint())
                    context.drawTexture(RenderPipelines.GLINT, knowledgeType.getSpriteIdentifier(id), getContentRightEnd() - 16 - 4, i, 0f, 0f, 16, 16, 16, 16);
                //context.drawTextWithShadow(KnowledgeScreen.this.textRenderer, id.toString(), getContentRightEnd() - KnowledgeScreen.this.textRenderer.getWidth(id.toString()) - 4, i, k);
            }
        }
    }
}
