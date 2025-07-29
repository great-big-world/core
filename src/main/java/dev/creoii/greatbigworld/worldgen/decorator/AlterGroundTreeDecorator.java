package dev.creoii.greatbigworld.worldgen.decorator;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.registry.GBWTreeDecoratorTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.ArrayList;

public class AlterGroundTreeDecorator extends TreeDecorator {
    public static final MapCodec<AlterGroundTreeDecorator> CODEC = BlockStateProvider.TYPE_CODEC.fieldOf("provider").xmap(AlterGroundTreeDecorator::new, decorator -> decorator.provider);
    private final BlockStateProvider provider;

    public AlterGroundTreeDecorator(BlockStateProvider provider) {
        this.provider = provider;
    }

    @Override
    protected TreeDecoratorType<?> getType() {
        return GBWTreeDecoratorTypes.ALTER_GROUND;
    }

    @Override
    public void generate(TreeDecorator.Generator generator) {
        ArrayList<BlockPos> list = Lists.newArrayList();
        ObjectArrayList<BlockPos> list2 = generator.getRootPositions();
        ObjectArrayList<BlockPos> list3 = generator.getLogPositions();
        if (list2.isEmpty()) {
            list.addAll(list3);
        } else if (!list3.isEmpty() && list2.getFirst().getY() == list3.getFirst().getY()) {
            list.addAll(list3);
            list.addAll(list2);
        } else list.addAll(list2);

        if (list.isEmpty())
            return;

        int i = list.getFirst().getY();
        list.stream().filter(pos -> pos.getY() == i).forEach(pos -> {
            setArea(generator, pos.west().north());
            setArea(generator, pos.east(2).north());
            setArea(generator, pos.west().south(2));
            setArea(generator, pos.east(2).south(2));
            for (int i1 = 0; i1 < 3; ++i1) {
                int j = generator.getRandom().nextInt(48);
                int k = j % 6;
                int l = j / 6;
                if (k != 0 && k != 5 && l != 0 && l != 5)
                    continue;
                setArea(generator, pos.add(-2 + k, 0, -2 + l));
            }
        });
    }

    private void setArea(TreeDecorator.Generator generator, BlockPos origin) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (Math.abs(i) == 2 && Math.abs(j) == 2)
                    continue;
                setColumn(generator, origin.add(i, 0, j));
            }
        }
    }

    private void setColumn(TreeDecorator.Generator generator, BlockPos origin) {
        for (int i = 2; i >= -3; --i) {
            BlockPos blockPos = origin.up(i);
            if (Feature.isSoil(generator.getWorld(), blockPos)) {
                generator.replace(blockPos, provider.get(generator.getRandom(), origin));
                break;
            }
            if (!generator.isAir(blockPos) && i < 0)
                break;
        }
    }
}
