package com.iwosw.vivariumlibera.block;

import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import com.iwosw.vivariumlibera.block.entity.AlchemyTablePartBlockEntity;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class AlchemyTableBlock extends BaseEntityBlock {
    public static final MapCodec<AlchemyTableBlock> CODEC = simpleCodec(AlchemyTableBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty HAS_LECTERN = BooleanProperty.create("has_lectern");

    private static final VoxelShape BASE_NORTH_SHAPE = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 32.0, 17.5, 32.0),
            Block.box(0.0, 17.5, 27.0, 23.0, 27.0, 32.0),
            Block.box(23.0, 17.5, 27.0, 32.0, 22.0, 32.0),
            Block.box(5.0, 17.5, 7.0, 23.0, 20.2, 20.0));
    private static final VoxelShape LECTERN_NORTH_SHAPE = Block.box(25.0, 17.5, 0.5, 31.0, 22.5, 7.0);
    private static final Map<Direction, VoxelShape> BASE_SHAPES = createShapes(BASE_NORTH_SHAPE);
    private static final Map<Direction, VoxelShape> LECTERN_SHAPES = createShapes(LECTERN_NORTH_SHAPE);

    public AlchemyTableBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
                .setValue(HAS_LECTERN, true));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTableBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
        for (BlockPos partPos : getPartPositions(context.getClickedPos(), state)) {
            if (!context.getLevel().getBlockState(partPos).canBeReplaced(context)) {
                return null;
            }
        }
        return state;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide()) {
            placePartBlocks(level, pos, state);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity table)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.is(Items.FLINT_AND_STEEL) && isCoalHit(state, pos, hitResult)) {
            if (!state.getValue(LIT) && !level.isClientSide()) {
                level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
                        level.getRandom().nextFloat() * 0.4F + 0.8F);
                level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
                stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.is(Items.LECTERN) && !state.getValue(HAS_LECTERN)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(HAS_LECTERN, true), Block.UPDATE_ALL);
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 0.8F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (isJug(stack) && !table.hasJug()) {
            if (!level.isClientSide() && table.placeJug(stack) && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (player.isShiftKeyDown() && table.hasJug() && isJugHit(state, pos, hitResult)) {
            returnJug(level, pos, player, table);
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (player.isShiftKeyDown() && table.hasBook() && isLecternHit(state, pos, hitResult)) {
            returnBook(level, pos, player, table);
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (stack.is(ModItems.HERBALIST_BOOK.get()) && state.getValue(HAS_LECTERN) && !table.hasBook()) {
            if (!level.isClientSide() && table.placeBook(stack) && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (level.isClientSide()) {
            openAlchemyTableScreen(pos);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult) {
        if (!(level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity table)) {
            return InteractionResult.PASS;
        }

        if (player.isShiftKeyDown() && table.hasJug() && isJugHit(state, pos, hitResult)) {
            returnJug(level, pos, player, table);
        } else if (player.isShiftKeyDown() && table.hasBook() && isLecternHit(state, pos, hitResult)) {
            returnBook(level, pos, player, table);
        } else if (level.isClientSide()) {
            openAlchemyTableScreen(pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity table) {
            removePartBlocks(level, pos, state);
            table.dropContents(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        placePartBlocks(level, pos, state);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape base = BASE_SHAPES.get(state.getValue(FACING));
        return state.getValue(HAS_LECTERN)
                ? Shapes.or(base, LECTERN_SHAPES.get(state.getValue(FACING)))
                : base;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT, HAS_LECTERN);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!state.getValue(HAS_LECTERN) || !isLecternTargeted(state, pos, player)) {
            return;
        }

        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof AlchemyTableBlockEntity table) {
                returnStoredItem(level, pos, player, table.removeBook());
            }
            level.setBlock(pos, state.setValue(HAS_LECTERN, false), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 0.9F, 1.0F);
            level.levelEvent(player, 2001, pos, Block.getId(Blocks.LECTERN.defaultBlockState()));
            if (!player.getAbilities().instabuild) {
                Block.popResource(level, pos, new ItemStack(Items.LECTERN));
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!state.getValue(LIT)) {
            return;
        }

        double localX = 0.875 + (random.nextDouble() - 0.5) * 0.45;
        double localZ = 0.84 + (random.nextDouble() - 0.5) * 0.35;
        double[] world = modelToWorld(state.getValue(FACING), localX, localZ);
        double x = pos.getX() + world[0];
        double y = pos.getY() + 1.27;
        double z = pos.getZ() + world[1];

        level.addParticle(ParticleTypes.SMOKE, x, y + 0.08, z, 0.0, 0.025, 0.0);
        if (random.nextInt(3) == 0) {
            level.addParticle(ParticleTypes.FLAME, x, y, z, 0.0, 0.012, 0.0);
        }
        if (random.nextInt(12) == 0) {
            level.playLocalSound(x, y, z, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                    0.35F, 0.9F + random.nextFloat() * 0.2F, false);
        }
    }

    private static Map<Direction, VoxelShape> createShapes(VoxelShape northShape) {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            shapes.put(direction, rotateShape(Direction.NORTH, direction, northShape));
        }
        return shapes;
    }

    public static void schedulePartRefresh(Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 1);
        }
    }

    private static List<BlockPos> getPartPositions(BlockPos masterPos, BlockState state) {
        List<BlockPos> positions = new ArrayList<>(3);
        for (int x = 0; x <= 1; x++) {
            for (int z = 0; z <= 1; z++) {
                if (x == 0 && z == 0) {
                    continue;
                }
                positions.add(masterPos.offset(x, 0, z));
            }
        }
        return positions;
    }

    private static void placePartBlocks(Level level, BlockPos masterPos, BlockState state) {
        List<BlockPos> desiredParts = getPartPositions(masterPos, state);
        removeStalePartBlocks(level, masterPos, desiredParts);
        for (BlockPos partPos : desiredParts) {
            BlockState current = level.getBlockState(partPos);
            if (current.is(ModBlocks.ALCHEMY_TABLE_PART.get())) {
                if (level.getBlockEntity(partPos) instanceof AlchemyTablePartBlockEntity part) {
                    part.setMasterPos(masterPos);
                }
                continue;
            }
            if (!current.canBeReplaced()) {
                continue;
            }
            level.setBlock(partPos, ModBlocks.ALCHEMY_TABLE_PART.get().defaultBlockState(), Block.UPDATE_ALL);
            if (level.getBlockEntity(partPos) instanceof AlchemyTablePartBlockEntity part) {
                part.setMasterPos(masterPos);
            }
        }
    }

    private static void removePartBlocks(Level level, BlockPos masterPos, BlockState state) {
        removeStalePartBlocks(level, masterPos, List.of());
    }

    private static void removeStalePartBlocks(Level level, BlockPos masterPos, List<BlockPos> keep) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos candidate = masterPos.offset(x, 0, z);
                if (!keep.contains(candidate)
                        && level.getBlockEntity(candidate) instanceof AlchemyTablePartBlockEntity part
                        && part.getMasterPos().equals(masterPos)) {
                    level.removeBlock(candidate, false);
                }
            }
        }
    }

    private static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = {shape, Shapes.empty()};
        int rotations = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < rotations; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
                    buffer[1] = Shapes.or(buffer[1], Shapes.box(
                            2.0 - maxZ, minY, minX,
                            2.0 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }

    private static void returnBook(Level level, BlockPos pos, Player player, AlchemyTableBlockEntity table) {
        if (!level.isClientSide()) {
            returnStoredItem(level, pos, player, table.removeBook());
        }
    }

    private static void returnJug(Level level, BlockPos pos, Player player, AlchemyTableBlockEntity table) {
        if (!level.isClientSide()) {
            returnStoredItem(level, pos, player, table.removeJug());
        }
    }

    private static void returnStoredItem(Level level, BlockPos pos, Player player, ItemStack stack) {
        if (!level.isClientSide() && !stack.isEmpty() && !player.addItem(stack)) {
            Block.popResource(level, pos, stack);
        }
    }

    private static boolean isJug(ItemStack stack) {
        return ModItems.JUG_ITEMS.stream().anyMatch(jug -> stack.is(jug.get()));
    }

    private static boolean isCoalHit(BlockState state, BlockPos pos, BlockHitResult hitResult) {
        double[] local = hitToModel(state, pos, hitResult);
        return local[1] >= 1.08 && local[0] >= 0.42 && local[0] <= 1.34
                && local[2] >= 0.54 && local[2] <= 1.14;
    }

    private static boolean isJugHit(BlockState state, BlockPos pos, BlockHitResult hitResult) {
        double[] local = hitToModel(state, pos, hitResult);
        return local[1] >= 1.04 && local[0] >= -0.02 && local[0] <= 0.58
                && local[2] >= -0.02 && local[2] <= 0.58;
    }

    private static boolean isLecternHit(BlockState state, BlockPos pos, BlockHitResult hitResult) {
        double[] local = hitToModel(state, pos, hitResult);
        return local[1] >= 1.05 && local[0] >= 1.52 && local[0] <= 1.98
                && local[2] >= 0.0 && local[2] <= 0.48;
    }

    private static double[] hitToModel(BlockState state, BlockPos pos, BlockHitResult hitResult) {
        double worldX = hitResult.getLocation().x - pos.getX();
        double worldY = hitResult.getLocation().y - pos.getY();
        double worldZ = hitResult.getLocation().z - pos.getZ();
        double localX;
        double localZ;

        switch (state.getValue(FACING)) {
            case EAST -> {
                localX = worldZ;
                localZ = 2.0 - worldX;
            }
            case SOUTH -> {
                localX = 2.0 - worldX;
                localZ = 2.0 - worldZ;
            }
            case WEST -> {
                localX = 2.0 - worldZ;
                localZ = worldX;
            }
            default -> {
                localX = worldX;
                localZ = worldZ;
            }
        }

        return new double[]{localX, worldY, localZ};
    }

    private static boolean isLecternTargeted(BlockState state, BlockPos pos, Player player) {
        Vec3 start = player.getEyePosition();
        Vec3 end = start.add(player.getViewVector(1.0F).scale(6.0));
        Vec3 localStart = worldToModel(state.getValue(FACING), pos, start);
        Vec3 localEnd = worldToModel(state.getValue(FACING), pos, end);
        AABB lectern = new AABB(25.0 / 16.0, 17.4 / 16.0, 0.25 / 16.0,
                31.0 / 16.0, 23.0 / 16.0, 7.0 / 16.0);
        return lectern.clip(localStart, localEnd).isPresent();
    }

    private static Vec3 worldToModel(Direction facing, BlockPos pos, Vec3 point) {
        double worldX = point.x - pos.getX();
        double worldZ = point.z - pos.getZ();
        return switch (facing) {
            case EAST -> new Vec3(worldZ, point.y - pos.getY(), 2.0 - worldX);
            case SOUTH -> new Vec3(2.0 - worldX, point.y - pos.getY(), 2.0 - worldZ);
            case WEST -> new Vec3(2.0 - worldZ, point.y - pos.getY(), worldX);
            default -> new Vec3(worldX, point.y - pos.getY(), worldZ);
        };
    }

    private static double[] modelToWorld(Direction facing, double localX, double localZ) {
        return switch (facing) {
            case EAST -> new double[]{2.0 - localZ, localX};
            case SOUTH -> new double[]{2.0 - localX, 2.0 - localZ};
            case WEST -> new double[]{localZ, 2.0 - localX};
            default -> new double[]{localX, localZ};
        };
    }

    private static void openAlchemyTableScreen(BlockPos pos) {
        try {
            Class.forName("com.iwosw.vivariumlibera.client.AlchemyTableScreen")
                    .getMethod("open", BlockPos.class)
                    .invoke(null, pos);
        } catch (ReflectiveOperationException ignored) {
            // Dedicated servers do not load client screen classes.
        }
    }
}
