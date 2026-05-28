package com.iwosw.vivariumlibera.block.entity;

import com.iwosw.vivariumlibera.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class MortarBlockEntity extends BlockEntity implements GeoBlockEntity {
    public static final String CONTROLLER = "mortar_controller";
    public static final String GRIND_TRIGGER = "grind";

    private static final RawAnimation GRIND_ANIMATION = RawAnimation.begin().then("animation11", Animation.LoopType.PLAY_ONCE);
    private static final int SLOT = 0;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
    private int grindTicks;

    public MortarBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.MORTAR.get(), pos, blockState);
    }

    public ItemStack getStoredItem() {
        return items.get(SLOT);
    }

    public boolean hasStoredItem() {
        return !getStoredItem().isEmpty();
    }

    public boolean isGrinding() {
        return this.grindTicks > 0;
    }

    public boolean canAdd(ItemStack stack) {
        ItemStack storedItem = getStoredItem();
        if (storedItem.isEmpty()) {
            return true;
        }

        return ItemStack.isSameItemSameComponents(storedItem, stack) && storedItem.getCount() < storedItem.getMaxStackSize();
    }

    public boolean canStartGrinding(TagKey<Item> ingredientTag) {
        return grindTicks <= 0 && getStoredItem().is(ingredientTag);
    }

    public void startGrinding(int ticks) {
        grindTicks = ticks;
        setChanged();
    }

    public boolean tickGrinding() {
        if (grindTicks <= 0) {
            return false;
        }

        grindTicks--;
        setChanged();
        return grindTicks == 0;
    }

    public void stopGrinding() {
        if (grindTicks > 0) {
            grindTicks = 0;
            setChanged();
        }
    }

    public boolean addOne(ItemStack stack) {
        if (stack.isEmpty() || !canAdd(stack)) {
            return false;
        }

        ItemStack storedItem = getStoredItem();
        if (storedItem.isEmpty()) {
            items.set(SLOT, stack.copyWithCount(1));
        } else {
            storedItem.grow(1);
        }

        grindTicks = 0;
        sync();
        return true;
    }

    public void setStoredItem(ItemStack stack) {
        items.set(SLOT, stack);
        grindTicks = 0;
        sync();
    }

    public ItemStack removeStoredItem() {
        ItemStack storedItem = getStoredItem();
        if (storedItem.isEmpty()) {
            return ItemStack.EMPTY;
        }

        items.set(SLOT, ItemStack.EMPTY);
        grindTicks = 0;
        sync();
        return storedItem;
    }

    public void dropContents(Level level, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }

        ItemStack storedItem = removeStoredItem();
        if (!storedItem.isEmpty()) {
            Block.popResource(level, pos, storedItem);
        }
    }

    public void triggerGrindAnimation() {
        triggerAnim(CONTROLLER, GRIND_TRIGGER);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        if (grindTicks > 0) {
            tag.putInt("GrindTicks", grindTicks);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.set(SLOT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
        grindTicks = tag.getInt("GrindTicks");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, CONTROLLER, state -> PlayState.STOP)
                .triggerableAnim(GRIND_TRIGGER, GRIND_ANIMATION));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}
