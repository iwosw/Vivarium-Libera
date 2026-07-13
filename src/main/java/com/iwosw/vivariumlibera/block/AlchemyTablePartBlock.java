package com.iwosw.vivariumlibera.block;

import com.iwosw.vivariumlibera.block.entity.AlchemyTablePartBlockEntity;
import com.mojang.serialization.MapCodec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class AlchemyTablePartBlock extends BaseEntityBlock {
    public static final MapCodec<AlchemyTablePartBlock> CODEC = simpleCodec(AlchemyTablePartBlock::new);

    public AlchemyTablePartBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTablePartBlockEntity(pos, state);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide()
                && level.getBlockEntity(pos) instanceof AlchemyTablePartBlockEntity part
                && level.getBlockState(part.getMasterPos()).is(com.iwosw.vivariumlibera.registry.ModBlocks.ALCHEMY_TABLE.get())) {
            level.destroyBlock(part.getMasterPos(), !player.getAbilities().instabuild, player);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof AlchemyTablePartBlockEntity part) {
            BlockPos masterPos = part.getMasterPos();
            BlockState masterState = level.getBlockState(masterPos);
            if (masterState.getBlock() instanceof AlchemyTableBlock table) {
                table.attack(masterState, level, masterPos, player);
            }
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
        if (level.getBlockEntity(pos) instanceof AlchemyTablePartBlockEntity part) {
            BlockPos masterPos = part.getMasterPos();
            BlockState masterState = level.getBlockState(masterPos);
            if (masterState.getBlock() instanceof AlchemyTableBlock table) {
                return table.useItemOn(stack, masterState, level, masterPos, player, hand, redirect(hitResult, masterPos));
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AlchemyTablePartBlockEntity part) {
            BlockPos masterPos = part.getMasterPos();
            BlockState masterState = level.getBlockState(masterPos);
            if (masterState.getBlock() instanceof AlchemyTableBlock table) {
                return table.useWithoutItem(masterState, level, masterPos, player, redirect(hitResult, masterPos));
            }
        }
        return InteractionResult.PASS;
    }

    private static BlockHitResult redirect(BlockHitResult original, BlockPos masterPos) {
        return new BlockHitResult(original.getLocation(), original.getDirection(), masterPos, original.isInside());
    }
}
