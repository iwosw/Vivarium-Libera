package com.iwosw.vivariumlibera.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public final class StreamFeature extends Feature<StreamFeatureConfiguration> {
    private static final int CHUNK_SIZE = 16;
    private static final int CELL_PADDING = 8;
    private static final BlockState WATER = Blocks.WATER.defaultBlockState();
    private static final BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
    private static final BlockState DIRT = Blocks.DIRT.defaultBlockState();
    private static final BlockState COARSE_DIRT = Blocks.COARSE_DIRT.defaultBlockState();
    private static final BlockState DEBUG_MARKER = Blocks.YELLOW_WOOL.defaultBlockState();

    public StreamFeature(Codec<StreamFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<StreamFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        StreamFeatureConfiguration config = context.config();
        BlockPos origin = context.origin();

        int chunkMinX = SectionPos.blockToSectionCoord(origin.getX()) * CHUNK_SIZE;
        int chunkMinZ = SectionPos.blockToSectionCoord(origin.getZ()) * CHUNK_SIZE;
        int chunkMaxX = chunkMinX + CHUNK_SIZE - 1;
        int chunkMaxZ = chunkMinZ + CHUNK_SIZE - 1;
        int radius = config.radius();
        int cellSize = config.cellSizeBlocks();

        int minCellX = Math.floorDiv(chunkMinX - cellSize, cellSize);
        int maxCellX = Math.floorDiv(chunkMaxX + cellSize, cellSize);
        int minCellZ = Math.floorDiv(chunkMinZ - cellSize, cellSize);
        int maxCellZ = Math.floorDiv(chunkMaxZ + cellSize, cellSize);
        boolean placed = false;

        for (int cellX = minCellX; cellX <= maxCellX; cellX++) {
            for (int cellZ = minCellZ; cellZ <= maxCellZ; cellZ++) {
                long cellSeed = mix(level.getSeed(), cellX, cellZ);
                if (unit(cellSeed) >= config.cellChance()) {
                    continue;
                }

                placed |= placeCellStream(level, config, cellX, cellZ, cellSeed, chunkMinX, chunkMaxX, chunkMinZ, chunkMaxZ, radius);
            }
        }

        return placed;
    }

    private boolean placeCellStream(
            WorldGenLevel level,
            StreamFeatureConfiguration config,
            int cellX,
            int cellZ,
            long cellSeed,
            int chunkMinX,
            int chunkMaxX,
            int chunkMinZ,
            int chunkMaxZ,
            int radius
    ) {
        int cellSize = config.cellSizeBlocks();
        int minX = cellX * cellSize;
        int minZ = cellZ * cellSize;
        boolean eastWest = (cellSeed & 1L) == 0L;
        double crossOffset = randomRange(cellSeed, 8, cellSize * 0.28D, cellSize * 0.72D);
        double endOffset = randomRange(cellSeed, 16, cellSize * 0.28D, cellSize * 0.72D);
        double phase = randomRange(cellSeed, 24, 0.0D, Math.PI * 2.0D);
        double wiggle = randomRange(cellSeed, 32, cellSize * 0.06D, cellSize * 0.16D);
        int steps = Mth.clamp(cellSize + CELL_PADDING * 2, 48, 96);
        boolean placed = false;

        for (int step = 0; step <= steps; step++) {
            double t = step / (double) steps;
            double wave = Math.sin(t * Math.PI * 2.0D + phase) * wiggle;
            int x;
            int z;

            if (eastWest) {
                x = (int) Math.round(Mth.lerp(t, minX - CELL_PADDING, minX + cellSize + CELL_PADDING));
                z = (int) Math.round(Mth.lerp(t, minZ + crossOffset, minZ + endOffset) + wave);
            } else {
                x = (int) Math.round(Mth.lerp(t, minX + crossOffset, minX + endOffset) + wave);
                z = (int) Math.round(Mth.lerp(t, minZ - CELL_PADDING, minZ + cellSize + CELL_PADDING));
            }

            if (x < chunkMinX + radius || x > chunkMaxX - radius || z < chunkMinZ + radius || z > chunkMaxZ - radius) {
                continue;
            }

            placed |= carveAt(level, config, x, z, cellSeed + step);
        }

        return placed;
    }

    private boolean carveAt(WorldGenLevel level, StreamFeatureConfiguration config, int x, int z, long seed) {
        int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
        if (surfaceY < config.minY() || surfaceY > config.maxY()) {
            return false;
        }

        if (isTooSteep(level, x, z, surfaceY, config.maxLocalSlope())) {
            return false;
        }

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        cursor.set(x, surfaceY, z);
        BlockState surface = level.getBlockState(cursor);
        if (!canReplace(surface) || !surface.getFluidState().isEmpty()) {
            return false;
        }

        int radius = config.radius();
        int bedY = surfaceY - config.depth();
        boolean placed = false;

        for (int dx = -radius - 1; dx <= radius + 1; dx++) {
            for (int dz = -radius - 1; dz <= radius + 1; dz++) {
                int distanceSq = dx * dx + dz * dz;
                if (distanceSq <= radius * radius + radius) {
                    placed |= placeWaterColumn(level, cursor, x + dx, bedY, z + dz, seed + dx * 31L + dz * 17L);
                } else if (distanceSq <= (radius + 1) * (radius + 1) + radius) {
                    placed |= placeBank(level, cursor, x + dx, surfaceY, z + dz, seed + dx * 13L + dz * 7L, config.debugMarkers());
                }
            }
        }

        return placed;
    }

    private boolean placeWaterColumn(WorldGenLevel level, BlockPos.MutableBlockPos cursor, int x, int bedY, int z, long seed) {
        cursor.set(x, bedY, z);
        BlockState bed = level.getBlockState(cursor);
        if (!canReplace(bed) || !bed.getFluidState().isEmpty()) {
            return false;
        }

        level.setBlock(cursor, streamBed(seed), 2);
        cursor.set(x, bedY + 1, z);
        BlockState waterSpace = level.getBlockState(cursor);
        if (!canReplace(waterSpace) || !waterSpace.getFluidState().isEmpty()) {
            return false;
        }

        level.setBlock(cursor, WATER, 2);
        cursor.set(x, bedY + 2, z);
        if (canReplace(level.getBlockState(cursor))) {
            level.setBlock(cursor, Blocks.AIR.defaultBlockState(), 2);
        }

        return true;
    }

    private boolean placeBank(WorldGenLevel level, BlockPos.MutableBlockPos cursor, int x, int y, int z, long seed, boolean debugMarkers) {
        cursor.set(x, y, z);
        BlockState state = level.getBlockState(cursor);
        if (!canReplace(state) || !state.getFluidState().isEmpty() || state.isAir()) {
            return false;
        }

        level.setBlock(cursor, debugMarkers ? DEBUG_MARKER : bankBlock(seed), 2);
        return true;
    }

    private boolean isTooSteep(WorldGenLevel level, int x, int z, int surfaceY, int maxLocalSlope) {
        if (maxLocalSlope <= 0) {
            return false;
        }

        return Math.abs(level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x + 1, z) - 1 - surfaceY) > maxLocalSlope
                || Math.abs(level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x - 1, z) - 1 - surfaceY) > maxLocalSlope
                || Math.abs(level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z + 1) - 1 - surfaceY) > maxLocalSlope
                || Math.abs(level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z - 1) - 1 - surfaceY) > maxLocalSlope;
    }

    private static boolean canReplace(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

    private static BlockState streamBed(long seed) {
        return (mix(seed) & 3L) == 0L ? DIRT : GRAVEL;
    }

    private static BlockState bankBlock(long seed) {
        long value = mix(seed) & 3L;
        if (value == 0L) {
            return GRAVEL;
        }
        return value == 1L ? COARSE_DIRT : DIRT;
    }

    private static double randomRange(long seed, int salt, double min, double max) {
        return min + unit(mix(seed + salt * 0x9E3779B97F4A7C15L)) * (max - min);
    }

    private static double unit(long seed) {
        return (mix(seed) >>> 11) * 0x1.0p-53;
    }

    private static long mix(long seed, int x, int z) {
        long value = seed;
        value ^= x * 0x9E3779B97F4A7C15L;
        value ^= z * 0xC2B2AE3D27D4EB4FL;
        return mix(value);
    }

    private static long mix(long value) {
        value ^= value >>> 30;
        value *= 0xBF58476D1CE4E5B9L;
        value ^= value >>> 27;
        value *= 0x94D049BB133111EBL;
        value ^= value >>> 31;
        return value;
    }
}
