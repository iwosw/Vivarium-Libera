package com.iwosw.vivariumlibera.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class PlumLeavesBlock extends LeavesBlock {
    private final Supplier<? extends Block> emptyLeaves;
    private final Supplier<? extends ItemLike> fruit;

    public PlumLeavesBlock(BlockBehaviour.Properties properties, Supplier<? extends Block> emptyLeaves, Supplier<? extends ItemLike> fruit) {
        super(properties);
        this.emptyLeaves = emptyLeaves;
        this.fruit = fruit;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            popResource(level, pos, new ItemStack(this.fruit.get()));
            level.setBlock(pos, emptyState(state), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private BlockState emptyState(BlockState state) {
        return this.emptyLeaves.get().defaultBlockState()
                .setValue(DISTANCE, state.getValue(DISTANCE))
                .setValue(PERSISTENT, state.getValue(PERSISTENT))
                .setValue(WATERLOGGED, state.getValue(WATERLOGGED));
    }
}
