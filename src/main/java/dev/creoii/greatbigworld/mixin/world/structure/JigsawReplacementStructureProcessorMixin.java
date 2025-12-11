package dev.creoii.greatbigworld.mixin.world.structure;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.creoii.greatbigworld.registry.GBWBlocks;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JigsawReplacementProcessor.class)
public class JigsawReplacementStructureProcessorMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "processBlock", at = @At(value = "RETURN", ordinal = 4), cancellable = true)
    private void gbw$processStructureTriggerReplacement(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlaceSettings data, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
        BlockState blockState = currentBlockInfo.state();
        if (blockState.is(GBWBlocks.STRUCTURE_TRIGGER)) {
            if (currentBlockInfo.nbt() == null) {
                LOGGER.warn("Structure Trigger block at {} is missing nbt, will not replace", pos);
                cir.setReturnValue(currentBlockInfo);
            } else {
                String string = currentBlockInfo.nbt().getStringOr("final_state", "minecraft:air");

                BlockState blockState2 = null;
                try {
                    BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(world.holderLookup(Registries.BLOCK), string, true);
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
