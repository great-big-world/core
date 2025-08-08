package dev.creoii.greatbigworld.registry;

import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.world.decorator.AlterGroundTreeDecorator;
import dev.creoii.greatbigworld.world.decorator.BranchTreeDecorator;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

public final class GBWTreeDecoratorTypes {
    public static final TreeDecoratorType<BranchTreeDecorator> BRANCH = new TreeDecoratorType<>(BranchTreeDecorator.CODEC);
    public static final TreeDecoratorType<AlterGroundTreeDecorator> ALTER_GROUND = new TreeDecoratorType<>(AlterGroundTreeDecorator.CODEC);

    public static void register() {
        Registry.register(Registries.TREE_DECORATOR_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "branch"), BRANCH);
        Registry.register(Registries.TREE_DECORATOR_TYPE, Identifier.of(GreatBigWorld.NAMESPACE, "alter_ground"), ALTER_GROUND);
    }
}
