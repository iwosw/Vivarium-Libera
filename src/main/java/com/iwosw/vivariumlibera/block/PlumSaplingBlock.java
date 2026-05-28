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

        int height = 5 + random.nextInt(2);
        if (!hasRoom(level, pos, height)) {
            return;
        }

        level.removeBlock(pos, false);

        BlockState logState = ModBlocks.PLUM_LOG.get().defaultBlockState();
        BlockState leavesState = ModBlocks.PLUM_LEAVES.get().defaultBlockState()
                .setValue(LeavesBlock.PERSISTENT, true)
                .setValue(LeavesBlock.DISTANCE, 1);

        // Generate trunk
        for (int y = 0; y < height; y++) {
            level.setBlock(pos.above(y), logState, 3);
        }

        BlockPos trunkTop = pos.above(height - 1);
        java.util.List<BlockPos> leafCenters = new java.util.ArrayList<>();
        leafCenters.add(trunkTop);

        int numBranches = 3 + random.nextInt(2);
        int[][] dirs = { {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1} };
        java.util.List<int[]> dirList = new java.util.ArrayList<>(java.util.Arrays.asList(dirs));
        java.util.Collections.shuffle(dirList, new java.util.Random(random.nextLong()));

        for (int i = 0; i < numBranches; i++) {
            int[] d = dirList.get(i);
            int dx = d[0];
            int dz = d[1];

            BlockPos current = trunkTop;
            int branchLength = 2;
            for (int step = 0; step < branchLength; step++) {
                current = current.offset(dx, 0, dz);
                if (level.getBlockState(current).canBeReplaced()) {
                    level.setBlock(current, logState, 3);
                }
                leafCenters.add(current);
            }
        }

        // Place leaves in flat horizontal slices around each center:
        // Y+1: radius 2 slice
        // Y:   radius 3 slice
        for (BlockPos center : leafCenters) {
            placeSlice(level, center.above(1), 2, leavesState);
            placeSlice(level, center, 3, leavesState);
        }
    }

    private static boolean hasRoom(ServerLevel level, BlockPos pos, int height) {
        for (int y = 0; y <= height + 4; y++) {
            int radius = y < 2 ? 1 : 4;
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

    private static void placeSlice(ServerLevel level, BlockPos center, int radius, BlockState leavesState) {
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                int absX = Math.abs(x);
                int absZ = Math.abs(z);

                // Corners-removed logic
                if (absX == radius && absZ == radius) {
                    continue;
                }
                if (radius > 2) {
                    if ((absX == radius && absZ == radius - 1) || (absX == radius - 1 && absZ == radius)) {
                        continue;
                    }
                }

                BlockPos leafPos = center.offset(x, 0, z);
                if (level.getBlockState(leafPos).canBeReplaced()) {
                    level.setBlock(leafPos, leavesState, 3);
                }
            }
        }
    }
}
