package com.iwosw.vivariumlibera.event;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.registry.ModBlocks;
import com.iwosw.vivariumlibera.registry.ModItems;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = VivariumLibera.MOD_ID)
public final class PlantCuttingEvents {
    private static final int REGROW_TICKS = 1200;

    private static final List<CutDrop> CUT_DROPS = List.of(
            new CutDrop(ModBlocks.NETTLE, ModItems.NETTLE_CUT),
            new CutDrop(ModBlocks.HENBANE, ModItems.HENBANE_CUT),
            new CutDrop(ModBlocks.ST_JOHNS_WORT, ModItems.ST_JOHNS_WORT_CUT),
            new CutDrop(ModBlocks.DATURA, ModItems.DATURA_CUT),
            new CutDrop(ModBlocks.FIREWEED, ModItems.FIREWEED_CUT),
            new CutDrop(ModBlocks.CHICORY, ModItems.CHICORY_CUT),
            new CutDrop(ModBlocks.COMFREY, ModItems.COMFREY_CUT),
            new CutDrop(ModBlocks.EYEBRIGHT, ModItems.EYEBRIGHT_CUT),
            new CutDrop(ModBlocks.SAGE, ModItems.SAGE_CUT),
            new CutDrop(ModBlocks.CALENDULA, ModItems.CALENDULA_CUT),
            new CutDrop(ModBlocks.WOOD_SORREL_RED, ModItems.WOOD_SORREL_RED_CUT),
            new CutDrop(ModBlocks.WOOD_SORREL_YELLOW, ModItems.WOOD_SORREL_YELLOW_CUT),
            new CutDrop(ModBlocks.YARROW_WHITE, ModItems.YARROW_CUT),
            new CutDrop(ModBlocks.YARROW_RED, ModItems.YARROW_CUT),
            new CutDrop(ModBlocks.YARROW_PINK, ModItems.YARROW_CUT),
            new CutDrop(ModBlocks.YARROW_YELLOW, ModItems.YARROW_CUT),
            new CutDrop(ModBlocks.BELL_FLOWER_BLUE, ModItems.BELL_FLOWER_CUT),
            new CutDrop(ModBlocks.BELL_FLOWER_PINK, ModItems.BELL_FLOWER_CUT),
            new CutDrop(ModBlocks.BELL_FLOWER_VIOLET, ModItems.BELL_FLOWER_CUT),
            new CutDrop(ModBlocks.BELL_FLOWER_WHITE, ModItems.BELL_FLOWER_CUT),
            new CutDrop(ModBlocks.VALERIAN_WHITE, ModItems.VALERIAN_WHITE_CUT),
            new CutDrop(ModBlocks.VALERIAN_PINK, ModItems.VALERIAN_PINK_CUT),
            new CutDrop(ModBlocks.VALERIAN_RED, ModItems.VALERIAN_RED_CUT),
            new CutDrop(ModBlocks.CROWS_EYE, ModItems.CROWS_EYE_BERRIES));

    private PlantCuttingEvents() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack heldItem = event.getEntity().getItemInHand(event.getHand());
        if (!heldItem.is(ModItems.KNIFE.get())) {
            return;
        }

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        BlockPos plantPos = getPlantBasePos(pos, state);
        BlockState plantState = level.getBlockState(plantPos);
        CutDrop cutDrop = findCutDrop(plantState);
        if (cutDrop == null) {
            return;
        }

        if (!isBlooming(plantState)) {
            return;
        }

        if (level.isClientSide) {
            return;
        }

        event.setCanceled(true);

        int count = getCutDropCount(plantState);
        cutPlant(level, plantPos, plantState);
        Block.popResource(level, plantPos, new ItemStack(cutDrop.item().get(), count));
    }

    private static CutDrop findCutDrop(BlockState state) {
        for (CutDrop cutDrop : CUT_DROPS) {
            if (state.is(cutDrop.block().get())) {
                return cutDrop;
            }
        }

        return null;
    }

    private static int getCutDropCount(BlockState state) {
        int count = getIntegerProperty(state, "amount", 1);
        count = getIntegerProperty(state, "flower_amount", count);
        return Math.max(1, count);
    }

    private static BlockPos getPlantBasePos(BlockPos pos, BlockState state) {
        if (state.hasProperty(DoublePlantBlock.HALF) && state.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
            return pos.below();
        }

        return pos;
    }

    private static boolean isBlooming(BlockState state) {
        for (Property<?> property : state.getProperties()) {
            if (property.getName().equals("blooming")) {
                return getBooleanPropertyValue(state, property, true);
            }
        }

        return false;
    }

    private static int getIntegerProperty(BlockState state, String propertyName, int fallback) {
        for (Property<?> property : state.getProperties()) {
            if (property.getName().equals(propertyName)) {
                return getIntegerPropertyValue(state, property, fallback);
            }
        }

        return fallback;
    }

    private static <T extends Comparable<T>> int getIntegerPropertyValue(
            BlockState state,
            Property<T> property,
            int fallback) {
        T value = state.getValue(property);
        return value instanceof Integer integer ? integer : fallback;
    }

    private static <T extends Comparable<T>> boolean getBooleanPropertyValue(
            BlockState state,
            Property<T> property,
            boolean fallback) {
        T value = state.getValue(property);
        return value instanceof Boolean bool ? bool : fallback;
    }

    private static void cutPlant(Level level, BlockPos pos, BlockState state) {
        if (state.hasProperty(DoublePlantBlock.HALF)) {
            BlockPos upperPos = pos.above();
            removePlantHalf(level, upperPos, state.getBlock());
            setNotBlooming(level, pos, state);
            return;
        }

        setNotBlooming(level, pos, state);
    }

    private static void removePlantHalf(Level level, BlockPos pos, Block plant) {
        if (level.getBlockState(pos).is(plant)) {
            level.removeBlock(pos, false);
        }
    }

    private static void setNotBlooming(Level level, BlockPos pos, BlockState state) {
        BlockState cutState = state;
        for (Property<?> property : state.getProperties()) {
            if (property.getName().equals("blooming")) {
                cutState = setBooleanPropertyValue(state, property, false);
                break;
            }
        }

        level.setBlock(pos, cutState, 3);
        level.scheduleTick(pos, cutState.getBlock(), REGROW_TICKS);
    }

    private static <T extends Comparable<T>> BlockState setBooleanPropertyValue(
            BlockState state,
            Property<T> property,
            boolean value) {
        return state.setValue(property, property.getValue(String.valueOf(value)).orElseThrow());
    }

    private record CutDrop(Supplier<? extends Block> block, Supplier<? extends Item> item) {
    }
}
