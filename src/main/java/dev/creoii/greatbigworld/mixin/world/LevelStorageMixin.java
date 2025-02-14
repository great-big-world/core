package dev.creoii.greatbigworld.mixin.world;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.datafixer.Schemas;
import net.minecraft.util.path.SymlinkFinder;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

@Mixin(LevelStorage.class)
public class LevelStorageMixin {
    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static void gbw$changeSavesDirectoriesServer(Path path, CallbackInfoReturnable<LevelStorage> cir, @Local SymlinkFinder symlinkFinder) {
        cir.setReturnValue(new LevelStorage(path.resolve("../gbw"), path.resolve("../backups/gbw"), symlinkFinder, Schemas.getFixer()));
    }
}