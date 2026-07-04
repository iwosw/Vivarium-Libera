package com.iwosw.vivariumlibera.block;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.block.entity.MortarBlockEntity;
import com.iwosw.vivariumlibera.registry.ModBlockEntities;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class MortarBlock extends BaseEntityBlock {
    public static final MapCodec<MortarBlock> CODEC = simpleCodec(MortarBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final TagKey<Item> CUT_FLOWERS = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, "cut_flowers"));
    private static final ResourceLocation CULTIO_ASH = ResourceLocation.fromNamespaceAndPath("cultio", "ash");
    private static final int MAX_CULTIO_ASH_INGREDIENTS = 16;
    private static final int GRIND_RESULT_TICKS = 42;
    private static final VoxelShape SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 5.0, 11.0);

    public MortarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MortarBlockEntity(pos, state);
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
        if (player.isShiftKeyDown() && returnStoredItem(level, pos, player)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        if (canCreateGrindingResult(stack) && level.getBlockEntity(pos) instanceof MortarBlockEntity mortar) {
            if (!canAddGrindingIngredient(mortar, stack)) {
                return ItemInteractionResult.FAIL;
            }

            if (mortar.addOne(stack)) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
            }

            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }

        triggerAnimation(level, pos);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof MortarBlockEntity mortar && mortar.hasStoredItem()) {
            if (!player.isShiftKeyDown()) {
                triggerAnimation(level, pos);
                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            returnStoredItem(level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        triggerAnimation(level, pos);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof MortarBlockEntity mortar) {
            mortar.dropContents(level, pos);
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> blockEntityType) {
        return level.isClientSide()
                ? null
                : createTickerHelper(blockEntityType, ModBlockEntities.MORTAR.get(), MortarBlock::tickServer);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected BlockState rotate(BlockState state, net.minecraft.world.level.block.Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, net.minecraft.world.level.block.Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    private static void triggerAnimation(Level level, BlockPos pos) {
        if (!(level instanceof ServerLevel)) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof MortarBlockEntity mortar) {
            if (mortar.canStartGrinding() && canCreateGrindingResult(mortar.getStoredItem())) {
                mortar.startGrinding(GRIND_RESULT_TICKS);
            }

            mortar.triggerGrindAnimation();
        }
    }

    private static void tickServer(Level level, BlockPos pos, BlockState state, MortarBlockEntity mortar) {
        if (mortar.isGrinding() && level instanceof ServerLevel serverLevel) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.25;
            double z = pos.getZ() + 0.5;
            serverLevel.sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, mortar.getStoredItem()),
                    x, y, z,
                    2,
                    0.1, 0.05, 0.1,
                    0.05
            );
        }

        if (!mortar.tickGrinding()) {
            return;
        }

        ItemStack storedItem = mortar.getStoredItem();
        ItemStack result = createGrindingResult(storedItem);
        if (result.isEmpty()) {
            mortar.stopGrinding();
            return;
        }

        mortar.setStoredItem(result);

        if (level instanceof ServerLevel serverLevel) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.3;
            double z = pos.getZ() + 0.5;
            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    x, y, z,
                    10,
                    0.15, 0.1, 0.15,
                    0.02
            );
        }
    }

    private static ItemStack createPowder(ItemStack ingredient) {
        ItemStack powder = new ItemStack(ModItems.HERBAL_POWDER.get(), ingredient.getCount());
        powder.set(DataComponents.ITEM_NAME, Component.translatable(
                "item.vivariumlibera.herbal_powder.named",
                getPowderIngredientName(ingredient)));
        return powder;
    }

    private static boolean canCreateGrindingResult(ItemStack ingredient) {
        return ingredient.is(CUT_FLOWERS) || isCultioAshIngredient(ingredient);
    }

    private static boolean canAddGrindingIngredient(MortarBlockEntity mortar, ItemStack ingredient) {
        if (!mortar.canAdd(ingredient)) {
            return false;
        }

        return !isCultioAshIngredient(ingredient) || mortar.getStoredItem().getCount() < MAX_CULTIO_ASH_INGREDIENTS;
    }

    private static ItemStack createGrindingResult(ItemStack ingredient) {
        if (ingredient.is(CUT_FLOWERS)) {
            return createPowder(ingredient);
        }

        if (isCultioAshIngredient(ingredient)) {
            Item ash = BuiltInRegistries.ITEM.get(CULTIO_ASH);
            return ash == null || ash == Items.AIR ? ItemStack.EMPTY : new ItemStack(ash, ingredient.getCount() * 4);
        }

        return ItemStack.EMPTY;
    }

    private static boolean isCultioAshIngredient(ItemStack ingredient) {
        Item ash = BuiltInRegistries.ITEM.get(CULTIO_ASH);
        return ingredient.is(Items.CHARCOAL) && ash != null && ash != Items.AIR;
    }

    private static Component getPowderIngredientName(ItemStack ingredient) {
        ResourceLocation itemKey = BuiltInRegistries.ITEM.getKey(ingredient.getItem());
        if (!itemKey.getNamespace().equals(VivariumLibera.MOD_ID)) {
            return ingredient.getHoverName();
        }

        String itemPath = itemKey.getPath();
        if (itemPath.equals("bell_flower_cut") || itemPath.equals("yarrow_cut")) {
            return Component.translatable("item.vivariumlibera.herbal_powder.ingredient." + itemPath.substring(0, itemPath.length() - 4));
        }

        if (itemPath.endsWith("_cut")) {
            return Component.translatable("block." + VivariumLibera.MOD_ID + "." + itemPath.substring(0, itemPath.length() - 4));
        }

        return ingredient.getHoverName();
    }

    private static boolean returnStoredItem(Level level, BlockPos pos, Player player) {
        if (!(level.getBlockEntity(pos) instanceof MortarBlockEntity mortar) || !mortar.hasStoredItem()) {
            return false;
        }

        if (!level.isClientSide()) {
            ItemStack storedItem = mortar.removeStoredItem();
            if (!player.getInventory().add(storedItem)) {
                player.drop(storedItem, false);
            }
        }

        return true;
    }
}
