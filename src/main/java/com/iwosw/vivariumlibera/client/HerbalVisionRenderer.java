package com.iwosw.vivariumlibera.client;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.effect.ModEffects;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

/**
 * Client render for the Herbal Vision decoction: outlines the mod's plants
 * within range, drawn without depth testing so they glow through blocks.
 */
@EventBusSubscriber(modid = VivariumLibera.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class HerbalVisionRenderer {
    private static final int RADIUS_XZ = 8;
    private static final int RADIUS_Y = 5;
    private static Set<Block> plantBlocks;

    private HerbalVisionRenderer() {
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || !mc.player.hasEffect(ModEffects.HERBAL_VISION)) {
            return;
        }

        Level level = mc.level;
        Set<Block> plants = plantBlocks();
        Camera camera = event.getCamera();
        Vec3 cam = camera.getPosition();
        BlockPos origin = mc.player.blockPosition();

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = mc.renderBuffers().bufferSource();
        var consumer = buffers.getBuffer(ModRenderTypes.PLANT_GLOW);

        poseStack.pushPose();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        for (BlockPos pos : BlockPos.betweenClosed(
                origin.offset(-RADIUS_XZ, -RADIUS_Y, -RADIUS_XZ),
                origin.offset(RADIUS_XZ, RADIUS_Y, RADIUS_XZ))) {
            BlockState state = level.getBlockState(pos);
            if (!plants.contains(state.getBlock())) {
                continue;
            }
            VoxelShape shape = state.getShape(level, pos);
            AABB box = shape.isEmpty() ? new AABB(BlockPos.ZERO) : shape.bounds();
            box = box.move(pos).inflate(0.02D);
            LevelRenderer.renderLineBox(poseStack, consumer, box, 0.45F, 1.0F, 0.55F, 0.85F);
        }
        poseStack.popPose();
        buffers.endBatch(ModRenderTypes.PLANT_GLOW);
    }

    private static Set<Block> plantBlocks() {
        if (plantBlocks == null) {
            Set<Block> set = new HashSet<>();
            for (var item : ModItems.PLANT_ITEMS) {
                set.add(item.get().getBlock());
            }
            plantBlocks = set;
        }
        return plantBlocks;
    }
}
