package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.decorator.AlterGroundTreeDecorator;
import dev.creoii.greatbigworld.world.decorator.BranchTreeDecorator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public final class GBWTreeDecoratorTypes {
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<AlterGroundTreeDecorator> ALTER_GROUND = new TreeDecoratorType<>(AlterGroundTreeDecorator.CODEC);

    public static void register() {
        Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "branch"), BRANCH);
        Registry.register(BuiltInRegistries.TREE_DECORATOR_TYPE, Identifier.fromNamespaceAndPath(GreatBigWorld.NAMESPACE, "alter_ground"), ALTER_GROUND);
    }
}
