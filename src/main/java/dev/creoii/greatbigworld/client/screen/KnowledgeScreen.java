package dev.creoii.greatbigworld.client.screen;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.client.GreatBigWorldClient;
import dev.creoii.greatbigworld.knowledge.Knowledge;
import dev.creoii.greatbigworld.util.network.RequestKnowledgeC2S;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Environment(EnvType.CLIENT)
public class KnowledgeScreen extends Screen {
    private static final Component TITLE_TEXT = Component.translatable("gui.knowledge");
    private static final Identifier KNOWLEDGE_BOOK_ID = Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "textures/gui/knowledge_book.png");
    protected final Screen parent;

    final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    @Nullable
    private TabNavigationBar tabNavigationWidget;

    public KnowledgeScreen(Screen parent) {
        super(TITLE_TEXT);
        this.parent = parent;
    }

    public Screen getParent() {
        return parent;
    }

    protected void init() {
        tabNavigationWidget = TabNavigationBar.builder(tabManager, width).addTabs(Arrays.stream(Knowledge.Type.values()).map(this::getFromType).toArray(Tab[]::new)).build();
        addRenderableWidget(tabNavigationWidget);
        layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());

        if (!tabNavigationWidget.getTabs().isEmpty()) {
            for (int i = 0; i < tabNavigationWidget.getTabs().size(); ++i) {
                tabNavigationWidget.setTabActiveState(i, true);
            }
            layout.visitWidgets((child) -> {
                child.setTabOrderGroup(1);
                addRenderableWidget(child);
            });
            tabNavigationWidget.selectTab(0, false);
        }
        repositionElements();
        ClientPlayNetworking.send(RequestKnowledgeC2S.INSTANCE);
    }

    protected void repositionElements() {
        if (tabNavigationWidget != null) {
            tabNavigationWidget.setWidth(width);
            tabNavigationWidget.arrangeElements();
            int i = tabNavigationWidget.getRectangle().bottom();
            ScreenRectangle screenRect = new ScreenRectangle(0, i, width, height - layout.getFooterHeight() - i);
            tabNavigationWidget.getTabs().forEach(tab -> tab.visitChildren(child -> child.setHeight(screenRect.height())));
            tabManager.setTabArea(screenRect);
            layout.setHeaderHeight(i);
            layout.arrangeElements();
        }
    }

    public boolean keyPressed(KeyEvent input) {
        return tabNavigationWidget != null && tabNavigationWidget.keyPressed(input) || super.keyPressed(input);
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, height - layout.getFooterHeight(), 0f, 0f, width, 2, 32, 2);
    }

    protected void renderMenuBackground(GuiGraphics context) {
        context.blit(RenderPipelines.GUI_TEXTURED, CreateWorldScreen.TAB_HEADER_BACKGROUND, 0, 0, 0f, 0f, width, layout.getHeaderHeight(), 16, 16);
        renderMenuBackground(context, 0, layout.getHeaderHeight(), width, height);
        //context.blit(RenderPipelines.GUI_TEXTURED, KNOWLEDGE_BOOK_ID, 20 + (width / 2), 2, 0f, 0f, 256, 256, 256, 256);
    }

    public void onClose() {
        minecraft.setScreen(parent);
    }

    private Tab getFromType(Knowledge.Type type) {
        return new KnowledgeTab(type, type.getTranslatedPlural(), new KnowledgeListWidget(minecraft, type));
    }

    @Environment(EnvType.CLIENT)
    public class KnowledgeTab extends GridLayoutTab {
        private final Knowledge.Type type;
        protected final AbstractSelectionList<?> widget;

        public KnowledgeTab(Knowledge.Type type, Component title, final AbstractSelectionList<?> widget) {
            super(title);
            this.type = type;
            this.widget = widget;
        }

        public Knowledge.Type getType() {
            return type;
        }

        public void doLayout(ScreenRectangle tabArea) {
            widget.updateSizeAndPosition(KnowledgeScreen.this.width, KnowledgeScreen.this.layout.getContentHeight(), KnowledgeScreen.this.layout.getHeaderHeight());
            new ArrayList<>(KnowledgeScreen.this.children()).forEach(element -> {
                if (element instanceof KnowledgeListWidget)
                    KnowledgeScreen.this.removeWidget(element);
            });
            KnowledgeScreen.this.addRenderableWidget(widget);
            super.doLayout(tabArea);
        }
    }

    @Environment(EnvType.CLIENT)
    class KnowledgeListWidget extends AbstractSelectionList<KnowledgeListWidget.Entry> {
        private final Knowledge.Type knowledgeType;

        public KnowledgeListWidget(final Minecraft client, Knowledge.Type type) {
            super(client, KnowledgeScreen.this.width, KnowledgeScreen.this.layout.getContentHeight(), 33, 20);
            this.knowledgeType = type;

            ObjectArrayList<Knowledge> objectArrayList = new ObjectArrayList<>(GreatBigWorldClient.getKnowledge().getOrDefault(type, new HashSet<>()).iterator());
            if (!objectArrayList.isEmpty()) {
                objectArrayList.sort(Comparator.comparing(knowledge -> I18n.get(knowledge.data().toString())));

                for (Knowledge knowledge : objectArrayList) {
                    addEntry(new KnowledgeListWidget.Entry(knowledge.data()));
                }
            }
        }

        public int getRowWidth() {
            return 180;
        }

        protected void renderListBackground(GuiGraphics context) {
        }

        protected void renderListSeparators(GuiGraphics context) {
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput builder) {
        }

        @Override
        public @Nullable KnowledgeListWidget.Entry getFocused() {
            return super.getFocused();
        }

        @Environment(EnvType.CLIENT)
        class Entry extends ObjectSelectionList.Entry<KnowledgeScreen.KnowledgeListWidget.Entry> {
            private final Identifier id;

            Entry(Identifier id) {
                this.id = id;
            }

            @Override
            public void renderContent(GuiGraphics guiGraphics, int i, int j, boolean bl, float f) {
                Objects.requireNonNull(KnowledgeScreen.this.font);
                int i1 = getContentYMiddle() - 9 / 2;
                int i2 = getContentYMiddle() - 16 / 2;
                int j1 = KnowledgeListWidget.this.children().indexOf(this);
                int k = j1 % 2 == 0 ? -1 : -4539718;
                guiGraphics.drawString(KnowledgeScreen.this.font, Knowledge.Type.getDisplayName(knowledgeType, minecraft.level.registryAccess(), id), getContentX() + 2, i1, k);

                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, knowledgeType.getSpriteIdentifier(id), getContentRight() - 16, i2, 0f, 0f, 16, 16, 16, 16);
                if (knowledgeType.hasGlint())
                    guiGraphics.blit(RenderPipelines.GLINT, knowledgeType.getSpriteIdentifier(id), getContentRight() - 16, i2, 0f, 0f, 16, 16, 16, 16);
                //context.drawTextWithShadow(KnowledgeScreen.this.textRenderer, id.toString(), getContentRightEnd() - KnowledgeScreen.this.textRenderer.getWidth(id.toString()) - 4, i1, k);
            }

            public Component getNarration() {
                return Component.empty();
            }
        }
    }
}
