package dev.creoii.greatbigworld.client.screen;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class StructureTriggerScreen extends Screen {
    private static final Text GROUP_TEXT = Text.translatable("structure_trigger.group");
    private static final Text TARGET_TEXT = Text.translatable("structure_trigger.target");
    private static final Text FINAL_STATE_TEXT = Text.translatable("structure_trigger.final_state");
    private static final Text TRIGGER_TEXT = Text.translatable("structure_trigger.trigger");
    private static final Text TRIGGER_TYPE_INIT = Text.translatable("structure_trigger.trigger_type_init");
    private static final Text TRIGGER_TYPE_TICK = Text.translatable("structure_trigger.trigger_type_tick");
    private static final Text TICK_RATE_TEXT = Text.translatable("structure_trigger.tick_rate");
    private final StructureTriggerBlockEntity triggerBlock;
    private TextFieldWidget groupField;
    private ButtonWidget groupDataTypeButton;
    private TextFieldWidget targetField;
    private TextFieldWidget tickRateField;
    private TextFieldWidget finalStateField;
    private ButtonWidget doneButton;
    private ButtonWidget triggerButton;
    private ButtonWidget triggerTypeButton;

    public StructureTriggerScreen(StructureTriggerBlockEntity triggerBlock) {
        super(NarratorManager.EMPTY);
        this.triggerBlock = triggerBlock;
    }

    private void onDone() {
        updateServer();
        client.setScreen(null);
    }

    private void onCancel() {
        client.setScreen(null);
    }

    private void updateServer() {
        ClientPlayNetworking.send(new StructureTriggerBlockEntity.UpdateStructureTriggerC2S(triggerBlock.getPos(), groupField.getText(), Identifier.tryParse(groupDataTypeButton.getMessage().getString()), Identifier.of(targetField.getText()), triggerTypeButton.getMessage().getString().toUpperCase(), Integer.parseInt(tickRateField.getText()), finalStateField.getText()));
    }

    private void trigger() {
        ClientPlayNetworking.send(new StructureTriggerBlockEntity.StructureTriggerC2S(triggerBlock.getPos(), Identifier.of(targetField.getText()), triggerTypeButton.getMessage().getString().toUpperCase(), Integer.parseInt(tickRateField.getText())));
    }

    public void close() {
        onCancel();
    }

    protected void init() {
        groupField = new TextFieldWidget(textRenderer, width / 2 - 4 - 150, 45, 150, 20, GROUP_TEXT);
        groupField.setMaxLength(128);
        groupField.setText(triggerBlock.getGroup() == null ? "" : triggerBlock.getGroup().toString());
        groupField.setChangedListener(target -> updateDoneButtonState());
        addSelectableChild(groupField);
        groupDataTypeButton = addDrawableChild(ButtonWidget.builder(Text.literal(triggerBlock.getGroupDataType() == StructureTriggerBlockEntity.DEFAULT_DATA_TYPE ? "" : triggerBlock.getGroupDataType().toString()), button -> {
            int dataTypeI = GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.getRawId(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.get(Identifier.tryParse(button.getMessage().getString())));
            int nextI = (dataTypeI + 1) % GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.size();
            button.setMessage(Text.literal(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.getId(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.get(nextI)).toString()));
        }).dimensions(width / 2 + 4, 45, 150, 20).build());
        targetField = new TextFieldWidget(textRenderer, width / 2 - 153, 80, 300, 20, TARGET_TEXT);
        targetField.setMaxLength(128);
        targetField.setText(triggerBlock.getTarget().toString());
        targetField.setChangedListener(target -> updateDoneButtonState());
        addSelectableChild(targetField);
        finalStateField = new TextFieldWidget(textRenderer, width / 2 - 153, 115, 300, 20, FINAL_STATE_TEXT);
        finalStateField.setMaxLength(256);
        finalStateField.setText(triggerBlock.getFinalState());
        addSelectableChild(finalStateField);
        triggerTypeButton = addDrawableChild(ButtonWidget.builder(triggerBlock.getTriggerType() == StructureTriggerBlock.TriggerType.INIT ? TRIGGER_TYPE_INIT : TRIGGER_TYPE_TICK, button -> {
            if (button.getMessage() == TRIGGER_TYPE_INIT) {
                button.setMessage(TRIGGER_TYPE_TICK);
                tickRateField.visible = true;
                selectables.add(tickRateField);
            } else {
                button.setMessage(TRIGGER_TYPE_INIT);
                tickRateField.visible = false;
                selectables.remove(tickRateField);
            }
        }).dimensions(width / 2 - 4 - 150, 150, 150, 20).build());
        tickRateField = new TextFieldWidget(textRenderer, width / 2 + 4, 150, 150, 20, TICK_RATE_TEXT);
        tickRateField.setMaxLength(10);
        tickRateField.setText(String.valueOf(triggerBlock.getTickRate()));
        tickRateField.setChangedListener(target -> updateDoneButtonState());
        addSelectableChild(tickRateField);
        triggerButton = addDrawableChild(ButtonWidget.builder(TRIGGER_TEXT, button -> {
            onDone();
            trigger();
        }).dimensions(width / 2 - 105, 185, 210, 20).build());
        doneButton = addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> onDone()).dimensions(width / 2 - 4 - 150, 210, 150, 20).build());
        addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, button -> onCancel()).dimensions(width / 2 + 4, 210, 150, 20).build());
        updateDoneButtonState();
    }

    protected void setInitialFocus() {
        setInitialFocus(targetField);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        renderInGameBackground(context);
    }

    public static boolean isValidId(String id) {
        return Identifier.tryParse(id) != null;
    }

    private void updateDoneButtonState() {
        boolean flag = isValidId(targetField.getText());
        doneButton.active = flag;
        triggerButton.active = flag;
    }

    public void resize(MinecraftClient client, int width, int height) {
        String string2 = targetField.getText();
        String string3 = tickRateField.getText();
        String string4 = finalStateField.getText();
        init(client, width, height);
        targetField.setText(string2);
        tickRateField.setText(string3);
        finalStateField.setText(string4);
    }

    public boolean keyPressed(KeyInput input) {
        if (super.keyPressed(input)) {
            return true;
        } else if (!doneButton.active || input.getKeycode() != 257 && input.getKeycode() != 335) {
            return false;
        } else {
            onDone();
            return true;
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawTextWithShadow(textRenderer, GROUP_TEXT, width / 2 - 153, 35, 10526880);
        groupField.render(context, mouseX, mouseY, deltaTicks);
        context.drawTextWithShadow(textRenderer, TARGET_TEXT, width / 2 - 153, 70, 10526880);
        targetField.render(context, mouseX, mouseY, deltaTicks);

        if (triggerTypeButton.getMessage() == TRIGGER_TYPE_TICK) {
            context.drawTextWithShadow(textRenderer, TICK_RATE_TEXT, width - 150, 140, 10526880);
            tickRateField.render(context, mouseX, mouseY, deltaTicks);
        }

        context.drawTextWithShadow(textRenderer, FINAL_STATE_TEXT, width / 2 - 153, 105, 10526880);
        finalStateField.render(context, mouseX, mouseY, deltaTicks);
    }
}
