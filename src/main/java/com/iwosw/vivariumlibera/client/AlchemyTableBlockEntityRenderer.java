package com.iwosw.vivariumlibera.client;

import com.iwosw.vivariumlibera.block.AlchemyTableBlock;
import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public final class AlchemyTableBlockEntityRenderer implements BlockEntityRenderer<AlchemyTableBlockEntity> {
    private static final ResourceLocation BOOK_TEXTURE = ResourceLocation.withDefaultNamespace(
            "textures/entity/enchanting_table_book.png");
    private final BookModel bookModel;
    private final ItemRenderer itemRenderer;

    public AlchemyTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.bookModel = new BookModel(context.bakeLayer(ModelLayers.BOOK));
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(
            AlchemyTableBlockEntity table,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {
        if (table.getBlockState().getValue(AlchemyTableBlock.HAS_LECTERN) && table.hasBook()) {
            renderBook(table, poseStack, bufferSource, packedLight, packedOverlay);
        }

        ItemStack jug = table.getStoredJug();
        if (!jug.isEmpty()) {
            renderJug(table, jug, poseStack, bufferSource, packedLight);
        }
    }

    private void renderBook(
            AlchemyTableBlockEntity table,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay) {
        Direction facing = table.getBlockState().getValue(AlchemyTableBlock.FACING);
        poseStack.pushPose();
        poseStack.translate(1.0F, 0.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(modelRotation(facing)));
        // Center the smaller book on the custom rest instead of using lectern scale.
        poseStack.translate(0.75F, 1.305F, -0.765625F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(67.5F));
        poseStack.translate(0.0F, -0.125F, 0.0F);
        poseStack.scale(0.34F, 0.34F, 0.34F);

        bookModel.setupAnim(0.0F, 0.1F, 0.9F, 1.2F);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entitySolid(BOOK_TEXTURE));
        bookModel.render(poseStack, consumer, packedLight, packedOverlay, -1);
        poseStack.popPose();
    }

    private void renderJug(
            AlchemyTableBlockEntity table,
            ItemStack jug,
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight) {
        Direction facing = table.getBlockState().getValue(AlchemyTableBlock.FACING);
        poseStack.pushPose();
        poseStack.translate(1.0F, 0.0F, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(modelRotation(facing)));
        poseStack.translate(-0.75F, 1.10F, -0.75F);
        poseStack.scale(1.05F, 1.05F, 1.05F);
        itemRenderer.renderStatic(
                jug,
                ItemDisplayContext.FIXED,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                bufferSource,
                table.getLevel(),
                (int) table.getBlockPos().asLong());
        poseStack.popPose();
    }

    @Override
    public AABB getRenderBoundingBox(AlchemyTableBlockEntity table) {
        BlockPos pos = table.getBlockPos();
        return new AABB(
                pos.getX() - 1.0,
                pos.getY(),
                pos.getZ() - 1.0,
                pos.getX() + 2.0,
                pos.getY() + 2.0,
                pos.getZ() + 2.0);
    }

    private static float modelRotation(Direction facing) {
        return switch (facing) {
            case NORTH -> 0.0F;
            case EAST -> 90.0F;
            case SOUTH -> 180.0F;
            case WEST -> 270.0F;
            default -> 0.0F;
        };
    }
}
