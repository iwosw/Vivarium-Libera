package com.iwosw.vivariumlibera.registry;

import com.iwosw.vivariumlibera.VivariumLibera;
import java.util.List;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VivariumLibera.MOD_ID);

    public static final DeferredItem<BlockItem> WORMWOOD_ITEM = registerBlockItem("wormwood", ModBlocks.WORMWOOD);
    public static final DeferredItem<BlockItem> NETTLE_ITEM = registerBlockItem("nettle", ModBlocks.NETTLE);
    public static final DeferredItem<BlockItem> HENBANE_ITEM = registerBlockItem("henbane", ModBlocks.HENBANE);
    public static final DeferredItem<BlockItem> ST_JOHNS_WORT_ITEM = registerBlockItem("st_johns_wort", ModBlocks.ST_JOHNS_WORT);
    public static final DeferredItem<BlockItem> DATURA_ITEM = registerBlockItem("datura", ModBlocks.DATURA);
    public static final DeferredItem<BlockItem> FIREWEED_ITEM = registerBlockItem("fireweed", ModBlocks.FIREWEED);
    public static final DeferredItem<BlockItem> CALAMUS_ITEM = registerBlockItem("calamus", ModBlocks.CALAMUS);
    public static final DeferredItem<BlockItem> CATTAIL_ITEM = registerBlockItem("cattail", ModBlocks.CATTAIL);

    public static final DeferredItem<Item> KNIFE = ITEMS.register("knife", () -> new Item(new Item.Properties()));

    public static final List<DeferredItem<BlockItem>> PLANT_ITEMS = List.of(
            WORMWOOD_ITEM,
            NETTLE_ITEM,
            HENBANE_ITEM,
            ST_JOHNS_WORT_ITEM,
            DATURA_ITEM,
            FIREWEED_ITEM,
            CALAMUS_ITEM,
            CATTAIL_ITEM);

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static DeferredItem<BlockItem> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.registerSimpleBlockItem(name, block);
    }
}
