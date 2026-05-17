package com.iwosw.vivariumlibera.mixin.client.iris;

import com.iwosw.vivariumlibera.VivariumLibera;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.irisshaders.iris.shaderpack.materialmap.BlockMaterialMapping", remap = false)
public abstract class BlockMaterialMappingMixin {
    private static final String[] SMALL_PLANTS = {
            "wormwood",
            "nettle",
            "henbane",
            "st_johns_wort",
            "datura",
            "calamus",
            "calendula",
            "comfrey",
            "eyebright",
            "sage",
            "wood_sorrel_red",
            "wood_sorrel_yellow",
            "yarrow_white",
            "yarrow_red",
            "yarrow_pink",
            "yarrow_yellow",
            "lycoris",
            "crows_eye",
            "thistle",
            "mint"
    };

    private static final String[] TALL_PLANTS = {
            "fireweed",
            "chicory",
            "cattail",
            "bell_flower_blue",
            "bell_flower_pink",
            "bell_flower_violet",
            "bell_flower_white",
            "valerian_white",
            "valerian_pink",
            "valerian_red",
            "belladonna"
    };

    @Inject(method = "createBlockStateIdMap", at = @At("RETURN"), require = 0)
    private static void vivariumlibera$addPlantShaderIds(
            Int2ObjectLinkedOpenHashMap<?> blockPropertiesMap,
            Int2ObjectLinkedOpenHashMap<?> tagPropertiesMap,
            CallbackInfoReturnable<Object2IntMap<BlockState>> cir
    ) {
        Object2IntMap<BlockState> blockStateIds = cir.getReturnValue();
        int smallPlantId = findMappedId(blockStateIds, Blocks.SHORT_GRASS, Blocks.FERN, Blocks.DANDELION);
        int tallPlantId = findMappedId(blockStateIds, Blocks.TALL_GRASS, Blocks.LARGE_FERN, Blocks.SUNFLOWER);

        if (smallPlantId != -1) {
            addMissingBlockStates(blockStateIds, smallPlantId, SMALL_PLANTS);
        }

        if (tallPlantId != -1) {
            addMissingBlockStates(blockStateIds, tallPlantId, TALL_PLANTS);
        }
    }

    private static int findMappedId(Object2IntMap<BlockState> blockStateIds, Block... blocks) {
        for (Block block : blocks) {
            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                if (blockStateIds.containsKey(state)) {
                    return blockStateIds.getInt(state);
                }
            }
        }

        return -1;
    }

    private static void addMissingBlockStates(Object2IntMap<BlockState> blockStateIds, int id, String[] blockNames) {
        for (String blockName : blockNames) {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath(VivariumLibera.MOD_ID, blockName));
            if (block == Blocks.AIR) {
                continue;
            }

            for (BlockState state : block.getStateDefinition().getPossibleStates()) {
                blockStateIds.putIfAbsent(state, id);
            }
        }
    }
}
