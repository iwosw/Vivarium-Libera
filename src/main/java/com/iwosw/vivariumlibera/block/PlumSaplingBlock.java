package com.iwosw.vivariumlibera.block;

import com.iwosw.vivariumlibera.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class PlumSaplingBlock extends SaplingBlock {
    public PlumSaplingBlock(BlockBehaviour.Properties properties) {
        super(TreeGrower.BIRCH, properties);
    }

    @Override
    public void advanceTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
            return;
        }

        int height = 4 + random.nextInt(2);
        if (!hasRoom(level, pos, height)) {
            return;
        }

        level.removeBlock(pos, false);
        for (int y = 0; y < height; y++) {
            level.setBlock(pos.above(y), ModBlocks.PLUM_LOG.get().defaultBlockState(), 3);
        }

        BlockPos crownBase = pos.above(height - 1);
        placeLeaves(level, crownBase, 2);
        placeLeaves(level, crownBase.above(), 2);
        placeLeaves(level, crownBase.above(2), 1);
        placeLeaves(level, crownBase.above(3), 1);
    }

    private static boolean hasRoom(ServerLevel level, BlockPos pos, int height) {
        for (int y = 0; y <= height + 2; y++) {
            int radius = y < height - 1 ? 0 : 2;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos checkPos = pos.offset(x, y, z);
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }

                    if (!level.getBlockState(checkPos).canBeReplaced()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private static void placeLeaves(ServerLevel level, BlockPos center, int radius) {
        BlockState leaves = ModBlocks.PLUM_LEAVES.get().defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true)
                .setValue(LeavesBlock.DISTANCE, 1);
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                BlockPos leafPos = center.offset(x, 0, z);
                if (level.getBlockState(leafPos).canBeReplaced()) {
                    level.setBlock(leafPos, leaves, 3);
                }
            }
        }
    }
}
