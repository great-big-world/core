package dev.creoii.greatbigworld.world.decorator;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import dev.creoii.greatbigworld.registry.GBWTreeDecoratorTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

public class AlterGroundTreeDecorator extends TreeDecorator {
    public static final MapCodec<AlterGroundTreeDecorator> CODEC = BlockStateProvider.CODEC.fieldOf("provider").xmap(AlterGroundTreeDecorator::new, decorator -> decorator.provider);
    private final BlockStateProvider provider;

    public AlterGroundTreeDecorator(BlockStateProvider provider) {
        this.provider = provider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return GBWTreeDecoratorTypes.ALTER_GROUND;
    }

    @Override
    public void place(TreeDecorator.Context generator) {
        ArrayList<BlockPos> list = Lists.newArrayList();
        ObjectArrayList<BlockPos> list2 = generator.roots();
        ObjectArrayList<BlockPos> list3 = generator.logs();
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
                int j = generator.random().nextInt(48);
                int k = j % 6;
                int l = j / 6;
                if (k != 0 && k != 5 && l != 0 && l != 5)
                    continue;
                setArea(generator, pos.offset(-2 + k, 0, -2 + l));
            }
        });
    }

    private void setArea(TreeDecorator.Context generator, BlockPos origin) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (Math.abs(i) == 2 && Math.abs(j) == 2)
                    continue;
                setColumn(generator, origin.offset(i, 0, j));
            }
        }
    }

    private void setColumn(TreeDecorator.Context generator, BlockPos origin) {
        for (int i = 2; i >= -3; --i) {
            BlockPos blockPos = origin.above(i);
            if (Feature.isGrassOrDirt(generator.level(), blockPos)) {
                generator.setBlock(blockPos, provider.getState(generator.random(), origin));
                break;
            }
            if (!generator.isAir(blockPos) && i < 0)
                break;
        }
    }
}
