package dev.creoii.greatbigworld.client.screen;

import dev.creoii.greatbigworld.block.StructureTriggerBlock;
import dev.creoii.greatbigworld.block.entity.StructureTriggerBlockEntity;
import dev.creoii.greatbigworld.registry.GBWRegistries;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class StructureTriggerScreen extends Screen {
    private static final Component GROUP_TEXT = Component.translatable("structure_trigger.group");
    private static final Component TARGET_TEXT = Component.translatable("structure_trigger.target");
    private static final Component FINAL_STATE_TEXT = Component.translatable("structure_trigger.final_state");
    private static final Component TRIGGER_TEXT = Component.translatable("structure_trigger.trigger");
    private static final Component TRIGGER_TYPE_INIT = Component.translatable("structure_trigger.trigger_type_init");
    private static final Component TRIGGER_TYPE_TICK = Component.translatable("structure_trigger.trigger_type_tick");
    private static final Component TICK_RATE_TEXT = Component.translatable("structure_trigger.tick_rate");
    private final StructureTriggerBlockEntity triggerBlock;
    private EditBox groupField;
    private Button groupDataTypeButton;
    private EditBox targetField;
    private EditBox tickRateField;
    private EditBox finalStateField;
    private Button doneButton;
    private Button triggerButton;
    private Button triggerTypeButton;

    public StructureTriggerScreen(StructureTriggerBlockEntity triggerBlock) {
        super(GameNarrator.NO_TITLE);
        this.triggerBlock = triggerBlock;
    }

    private void onDone() {
        updateServer();
        minecraft.setScreen(null);
    }

    private void onCancel() {
        minecraft.setScreen(null);
    }

    private void updateServer() {
        ClientPlayNetworking.send(new StructureTriggerBlockEntity.UpdateStructureTriggerC2S(triggerBlock.getBlockPos(), groupField.getValue(), Identifier.tryParse(groupDataTypeButton.getMessage().getString()), Identifier.parse(targetField.getValue()), triggerTypeButton.getMessage().getString().toUpperCase(), Integer.parseInt(tickRateField.getValue()), finalStateField.getValue()));
    }

    private void trigger() {
        ClientPlayNetworking.send(new StructureTriggerBlockEntity.StructureTriggerC2S(triggerBlock.getBlockPos(), Identifier.parse(targetField.getValue()), triggerTypeButton.getMessage().getString().toUpperCase(), Integer.parseInt(tickRateField.getValue())));
    }

    public void onClose() {
        onCancel();
    }

    protected void init() {
        groupField = new EditBox(font, width / 2 - 4 - 150, 45, 150, 20, GROUP_TEXT);
        groupField.setMaxLength(128);
        groupField.setValue(triggerBlock.getGroup() == null ? "" : triggerBlock.getGroup().toString());
        groupField.setResponder(target -> updateDoneButtonState());
        addWidget(groupField);
        groupDataTypeButton = addRenderableWidget(Button.builder(Component.literal(triggerBlock.getGroupDataType() == StructureTriggerBlockEntity.DEFAULT_DATA_TYPE ? "" : triggerBlock.getGroupDataType().toString()), button -> {
            int dataTypeI = GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.getId(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.getValue(Identifier.tryParse(button.getMessage().getString())));
            int nextI = (dataTypeI + 1) % GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.size();
            button.setMessage(Component.literal(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.getKey(GBWRegistries.STRUCTURE_TRIGGER_DATA_TYPES.byId(nextI)).toString()));
        }).bounds(width / 2 + 4, 45, 150, 20).build());
        targetField = new EditBox(font, width / 2 - 153, 80, 300, 20, TARGET_TEXT);
        targetField.setMaxLength(128);
        targetField.setValue(triggerBlock.getTarget().toString());
        targetField.setResponder(target -> updateDoneButtonState());
        addWidget(targetField);
        finalStateField = new EditBox(font, width / 2 - 153, 115, 300, 20, FINAL_STATE_TEXT);
        finalStateField.setMaxLength(256);
        finalStateField.setValue(triggerBlock.getFinalState());
        addWidget(finalStateField);
        triggerTypeButton = addRenderableWidget(Button.builder(triggerBlock.getTriggerType() == StructureTriggerBlock.TriggerType.INIT ? TRIGGER_TYPE_INIT : TRIGGER_TYPE_TICK, button -> {
            if (button.getMessage() == TRIGGER_TYPE_INIT) {
                button.setMessage(TRIGGER_TYPE_TICK);
                tickRateField.visible = true;
                narratables.add(tickRateField);
            } else {
                button.setMessage(TRIGGER_TYPE_INIT);
                tickRateField.visible = false;
                narratables.remove(tickRateField);
            }
        }).bounds(width / 2 - 4 - 150, 150, 150, 20).build());
        tickRateField = new EditBox(font, width / 2 + 4, 150, 150, 20, TICK_RATE_TEXT);
        tickRateField.setMaxLength(10);
        tickRateField.setValue(String.valueOf(triggerBlock.getTickRate()));
        tickRateField.setResponder(target -> updateDoneButtonState());
        addWidget(tickRateField);
        triggerButton = addRenderableWidget(Button.builder(TRIGGER_TEXT, button -> {
            onDone();
            trigger();
        }).bounds(width / 2 - 105, 185, 210, 20).build());
        doneButton = addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> onDone()).bounds(width / 2 - 4 - 150, 210, 150, 20).build());
        addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> onCancel()).bounds(width / 2 + 4, 210, 150, 20).build());
        updateDoneButtonState();
    }

    protected void setInitialFocus() {
        setInitialFocus(targetField);
    }

    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        renderTransparentBackground(context);
    }

    public static boolean isValidId(String id) {
        return Identifier.tryParse(id) != null;
    }

    private void updateDoneButtonState() {
        boolean flag = isValidId(targetField.getValue());
        doneButton.active = flag;
        triggerButton.active = flag;
    }

    public void resize(int width, int height) {
        String string2 = targetField.getValue();
        String string3 = tickRateField.getValue();
        String string4 = finalStateField.getValue();
        init(width, height);
        targetField.setValue(string2);
        tickRateField.setValue(string3);
        finalStateField.setValue(string4);
    }

    public boolean keyPressed(KeyEvent input) {
        if (super.keyPressed(input)) {
            return true;
        } else if (!doneButton.active || input.input() != 257 && input.input() != 335) {
            return false;
        } else {
            onDone();
            return true;
        }
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        context.drawString(font, GROUP_TEXT, width / 2 - 153, 35, 10526880);
        groupField.render(context, mouseX, mouseY, deltaTicks);
        context.drawString(font, TARGET_TEXT, width / 2 - 153, 70, 10526880);
        targetField.render(context, mouseX, mouseY, deltaTicks);

        if (triggerTypeButton.getMessage() == TRIGGER_TYPE_TICK) {
            context.drawString(font, TICK_RATE_TEXT, width - 150, 140, 10526880);
            tickRateField.render(context, mouseX, mouseY, deltaTicks);
        }

        context.drawString(font, FINAL_STATE_TEXT, width / 2 - 153, 105, 10526880);
        finalStateField.render(context, mouseX, mouseY, deltaTicks);
    }
}
