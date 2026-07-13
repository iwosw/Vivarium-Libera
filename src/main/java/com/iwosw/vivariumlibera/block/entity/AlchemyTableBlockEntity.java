package com.iwosw.vivariumlibera.block.entity;

import com.iwosw.vivariumlibera.registry.ModBlockEntities;
import com.iwosw.vivariumlibera.block.AlchemyTableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class AlchemyTableBlockEntity extends BlockEntity {
    private static final int BOOK_SLOT = 0;
    private static final int JUG_SLOT = 1;
    private final NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_TABLE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null) {
            AlchemyTableBlock.schedulePartRefresh(level, worldPosition);
        }
    }

    public ItemStack getStoredBook() {
        return items.get(BOOK_SLOT);
    }

    public boolean hasBook() {
        return !getStoredBook().isEmpty();
    }

    public boolean placeBook(ItemStack stack) {
        if (stack.isEmpty() || hasBook()) {
            return false;
        }

        items.set(BOOK_SLOT, stack.copyWithCount(1));
        sync();
        return true;
    }

    public ItemStack getStoredJug() {
        return items.get(JUG_SLOT);
    }

    public boolean hasJug() {
        return !getStoredJug().isEmpty();
    }

    public boolean placeJug(ItemStack stack) {
        if (stack.isEmpty() || hasJug()) {
            return false;
        }

        items.set(JUG_SLOT, stack.copyWithCount(1));
        sync();
        return true;
    }

    public ItemStack removeJug() {
        ItemStack jug = getStoredJug();
        if (jug.isEmpty()) {
            return ItemStack.EMPTY;
        }

        items.set(JUG_SLOT, ItemStack.EMPTY);
        sync();
        return jug;
    }

    public ItemStack removeBook() {
        ItemStack book = getStoredBook();
        if (book.isEmpty()) {
            return ItemStack.EMPTY;
        }

        items.set(BOOK_SLOT, ItemStack.EMPTY);
        sync();
        return book;
    }

    public void dropBook(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack book = removeBook();
        if (!book.isEmpty()) {
            Block.popResource(level, pos, book);
        }
    }

    public void dropContents(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        ItemStack book = removeBook();
        ItemStack jug = removeJug();
        if (!book.isEmpty()) {
            Block.popResource(level, pos, book);
        }
        if (!jug.isEmpty()) {
            Block.popResource(level, pos, jug);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.set(BOOK_SLOT, ItemStack.EMPTY);
        items.set(JUG_SLOT, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}
