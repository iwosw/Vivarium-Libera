package com.iwosw.vivariumlibera.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelReader;

final class PlantPlacementRules {
    private PlantPlacementRules() {
    }

    static boolean hasNearbyWater(LevelReader level, BlockPos pos) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (level.getFluidState(pos.relative(direction)).is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }

    static boolean hasWaterAtOrNearby(LevelReader level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER)
                || hasNearbyWater(level, pos)
                // Shore plants stand above their support, while water is beside that support.
                || hasNearbyWater(level, pos.below());
    }
}
