package dev.creoii.greatbigworld.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.validation.DirectoryValidator;

@Mixin(LevelStorageSource.class)
public class LevelStorageMixin {
    @Inject(method = "createDefault", at = @At("RETURN"), cancellable = true)
    private static void gbw$changeSavesDirectoriesServer(Path path, CallbackInfoReturnable<LevelStorageSource> cir, @Local DirectoryValidator symlinkFinder) {
        cir.setReturnValue(new LevelStorageSource(path.resolve("../gbw"), path.resolve("../backups/gbw"), symlinkFinder, DataFixers.getDataFixer()));
    }
}