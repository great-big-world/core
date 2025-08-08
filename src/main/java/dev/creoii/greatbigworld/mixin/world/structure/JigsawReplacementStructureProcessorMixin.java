package dev.creoii.greatbigworld.mixin.world.structure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.creoii.greatbigworld.registry.GBWBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JigsawReplacementStructureProcessor.class)
public class JigsawReplacementStructureProcessorMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "process", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void gbw$processStructureTriggerReplacement(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        BlockState blockState = currentBlockInfo.state();
        if (blockState.isOf(GBWBlocks.STRUCTURE_TRIGGER)) {
            if (currentBlockInfo.nbt() == null) {
                LOGGER.warn("Structure Trigger block at {} is missing nbt, will not replace", pos);
                cir.setReturnValue(currentBlockInfo);
            } else {
                String string = currentBlockInfo.nbt().getString("final_state", "minecraft:air");

                BlockState blockState2 = null;
                try {
                    BlockArgumentParser.BlockResult blockResult = BlockArgumentParser.block(world.createCommandRegistryWrapper(RegistryKeys.BLOCK), string, true);
                    blockState2 = blockResult.blockState();
                } catch (CommandSyntaxException commandSyntaxException) {
                    LOGGER.error("Failed to parse structure trigger replacement state '{}' at {}: {}", string, pos, commandSyntaxException.getMessage());
                    cir.setReturnValue(null);
                }
                cir.setReturnValue(new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), blockState2, null));
            }
        }
    }
}
