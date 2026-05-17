package com.iwosw.vivariumlibera.event;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = VivariumLibera.MOD_ID)
public final class WoodToolEvents {
    private WoodToolEvents() {
    }

    @SubscribeEvent
    public static void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility() != ItemAbilities.AXE_STRIP) {
            return;
        }

        BlockState state = event.getState();
        if (!state.is(ModBlocks.PLUM_LOG.get())) {
            return;
        }

        event.setFinalState(ModBlocks.STRIPPED_PLUM_LOG.get().defaultBlockState()
                .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)));
    }
}
