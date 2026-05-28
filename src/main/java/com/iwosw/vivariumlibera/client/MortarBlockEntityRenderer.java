package com.iwosw.vivariumlibera.client;

import com.iwosw.vivariumlibera.block.MortarBlock;
import com.iwosw.vivariumlibera.block.entity.MortarBlockEntity;
import com.iwosw.vivariumlibera.registry.ModBlockEntities;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public final class MortarBlockEntityRenderer extends GeoBlockRenderer<MortarBlockEntity> {
    private final ItemRenderer itemRenderer;

    public MortarBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(ModBlockEntities.MORTAR.get());
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
            MortarBlockEntity mortar,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {
        super.render(mortar, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        ItemStack storedItem = mortar.getStoredItem();
        if (storedItem.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        Direction facing = mortar.getBlockState().getValue(MortarBlock.FACING);
        poseStack.translate(0.5F, 0.16F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(0.24F, 0.24F, 0.24F);
        itemRenderer.renderStatic(
                storedItem,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                mortar.getLevel(),
                (int) mortar.getBlockPos().asLong());
        poseStack.popPose();
    }
}
