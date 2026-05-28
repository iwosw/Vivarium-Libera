package com.iwosw.vivariumlibera.registry;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.item.HerbalistBookItem;
import java.util.List;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VivariumLibera.MOD_ID);
    private static final FoodProperties RIPE_PLUM_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(0.3F)
            .build();

    public static final DeferredItem<BlockItem> WORMWOOD_ITEM = registerBlockItem("wormwood", ModBlocks.WORMWOOD);
    public static final DeferredItem<BlockItem> NETTLE_ITEM = registerBlockItem("nettle", ModBlocks.NETTLE);
    public static final DeferredItem<BlockItem> HENBANE_ITEM = registerBlockItem("henbane", ModBlocks.HENBANE);
    public static final DeferredItem<BlockItem> ST_JOHNS_WORT_ITEM = registerBlockItem("st_johns_wort", ModBlocks.ST_JOHNS_WORT);
    public static final DeferredItem<BlockItem> DATURA_ITEM = registerBlockItem("datura", ModBlocks.DATURA);
    public static final DeferredItem<BlockItem> FIREWEED_ITEM = registerBlockItem("fireweed", ModBlocks.FIREWEED);
    public static final DeferredItem<BlockItem> CHICORY_ITEM = registerBlockItem("chicory", ModBlocks.CHICORY);
    public static final DeferredItem<BlockItem> CALAMUS_ITEM = registerBlockItem("calamus", ModBlocks.CALAMUS);
    public static final DeferredItem<BlockItem> CATTAIL_ITEM = registerBlockItem("cattail", ModBlocks.CATTAIL);
    public static final DeferredItem<BlockItem> CALENDULA_ITEM = registerBlockItem("calendula", ModBlocks.CALENDULA);
    public static final DeferredItem<BlockItem> COMFREY_ITEM = registerBlockItem("comfrey", ModBlocks.COMFREY);
    public static final DeferredItem<BlockItem> EYEBRIGHT_ITEM = registerBlockItem("eyebright", ModBlocks.EYEBRIGHT);
    public static final DeferredItem<BlockItem> SAGE_ITEM = registerBlockItem("sage", ModBlocks.SAGE);
    public static final DeferredItem<BlockItem> WOOD_SORREL_RED_ITEM = registerBlockItem("wood_sorrel_red", ModBlocks.WOOD_SORREL_RED);
    public static final DeferredItem<BlockItem> WOOD_SORREL_YELLOW_ITEM = registerBlockItem("wood_sorrel_yellow", ModBlocks.WOOD_SORREL_YELLOW);
    public static final DeferredItem<BlockItem> YARROW_WHITE_ITEM = registerBlockItem("yarrow_white", ModBlocks.YARROW_WHITE);
    public static final DeferredItem<BlockItem> YARROW_RED_ITEM = registerBlockItem("yarrow_red", ModBlocks.YARROW_RED);
    public static final DeferredItem<BlockItem> YARROW_PINK_ITEM = registerBlockItem("yarrow_pink", ModBlocks.YARROW_PINK);
    public static final DeferredItem<BlockItem> YARROW_YELLOW_ITEM = registerBlockItem("yarrow_yellow", ModBlocks.YARROW_YELLOW);
    public static final DeferredItem<BlockItem> LYCORIS_ITEM = registerBlockItem("lycoris", ModBlocks.LYCORIS);
    public static final DeferredItem<BlockItem> BELL_FLOWER_BLUE_ITEM = registerBlockItem("bell_flower_blue", ModBlocks.BELL_FLOWER_BLUE);
    public static final DeferredItem<BlockItem> BELL_FLOWER_PINK_ITEM = registerBlockItem("bell_flower_pink", ModBlocks.BELL_FLOWER_PINK);
    public static final DeferredItem<BlockItem> BELL_FLOWER_VIOLET_ITEM = registerBlockItem("bell_flower_violet", ModBlocks.BELL_FLOWER_VIOLET);
    public static final DeferredItem<BlockItem> BELL_FLOWER_WHITE_ITEM = registerBlockItem("bell_flower_white", ModBlocks.BELL_FLOWER_WHITE);
    public static final DeferredItem<BlockItem> VALERIAN_WHITE_ITEM = registerBlockItem("valerian_white", ModBlocks.VALERIAN_WHITE);
    public static final DeferredItem<BlockItem> VALERIAN_PINK_ITEM = registerBlockItem("valerian_pink", ModBlocks.VALERIAN_PINK);
    public static final DeferredItem<BlockItem> VALERIAN_RED_ITEM = registerBlockItem("valerian_red", ModBlocks.VALERIAN_RED);
    public static final DeferredItem<BlockItem> CROWS_EYE_ITEM = registerBlockItem("crows_eye", ModBlocks.CROWS_EYE);
    public static final DeferredItem<BlockItem> THISTLE_ITEM = registerBlockItem("thistle", ModBlocks.THISTLE);
    public static final DeferredItem<BlockItem> MINT_ITEM = registerBlockItem("mint", ModBlocks.MINT);
    public static final DeferredItem<BlockItem> BELLADONNA_ITEM = registerBlockItem("belladonna", ModBlocks.BELLADONNA);
    public static final DeferredItem<BlockItem> PLUM_PLANKS_ITEM = registerBlockItem("plum_planks", ModBlocks.PLUM_PLANKS);
    public static final DeferredItem<BlockItem> PLUM_SLAB_ITEM = registerBlockItem("plum_slab", ModBlocks.PLUM_SLAB);
    public static final DeferredItem<BlockItem> PLUM_STAIRS_ITEM = registerBlockItem("plum_stairs", ModBlocks.PLUM_STAIRS);
    public static final DeferredItem<BlockItem> PLUM_BUTTON_ITEM = registerBlockItem("plum_button", ModBlocks.PLUM_BUTTON);
    public static final DeferredItem<BlockItem> PLUM_PRESSURE_PLATE_ITEM = registerBlockItem("plum_pressure_plate", ModBlocks.PLUM_PRESSURE_PLATE);
    public static final DeferredItem<BlockItem> PLUM_FENCE_ITEM = registerBlockItem("plum_fence", ModBlocks.PLUM_FENCE);
    public static final DeferredItem<BlockItem> PLUM_FENCE_GATE_ITEM = registerBlockItem("plum_fence_gate", ModBlocks.PLUM_FENCE_GATE);
    public static final DeferredItem<BlockItem> PLUM_TRAPDOOR_ITEM = registerBlockItem("plum_trapdoor", ModBlocks.PLUM_TRAPDOOR);
    public static final DeferredItem<BlockItem> PLUM_LOG_ITEM = registerBlockItem("plum_log", ModBlocks.PLUM_LOG);
    public static final DeferredItem<BlockItem> STRIPPED_PLUM_LOG_ITEM = registerBlockItem("stripped_plum_log", ModBlocks.STRIPPED_PLUM_LOG);
    public static final DeferredItem<BlockItem> PLUM_LEAVES_ITEM = registerBlockItem("plum_leaves", ModBlocks.PLUM_LEAVES);
    public static final DeferredItem<BlockItem> PLUM_LEAVES_1_ITEM = registerBlockItem("plum_leaves_1", ModBlocks.PLUM_LEAVES_1);
    public static final DeferredItem<BlockItem> PLUM_LEAVES_2_ITEM = registerBlockItem("plum_leaves_2", ModBlocks.PLUM_LEAVES_2);
    public static final DeferredItem<BlockItem> PLUM_SAPLING_ITEM = registerBlockItem("plum_sapling", ModBlocks.PLUM_SAPLING);
    public static final DeferredItem<BlockItem> MORTAR_ITEM = registerBlockItem("mortar", ModBlocks.MORTAR);

    public static final DeferredItem<Item> KNIFE = ITEMS.register("knife", () -> new Item(new Item.Properties()));
    public static final DeferredItem<HerbalistBookItem> HERBALIST_BOOK = ITEMS.register("herbalist_book", () -> new HerbalistBookItem(
            new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.WRITTEN_BOOK_CONTENT, HerbalistBookItem.createContent())));

    public static final DeferredItem<Item> WORMWOOD_CUT = registerSimpleItem("wormwood_cut");
    public static final DeferredItem<Item> NETTLE_CUT = registerSimpleItem("nettle_cut");
    public static final DeferredItem<Item> HENBANE_CUT = registerSimpleItem("henbane_cut");
    public static final DeferredItem<Item> ST_JOHNS_WORT_CUT = registerSimpleItem("st_johns_wort_cut");
    public static final DeferredItem<Item> DATURA_CUT = registerSimpleItem("datura_cut");
    public static final DeferredItem<Item> FIREWEED_CUT = registerSimpleItem("fireweed_cut");
    public static final DeferredItem<Item> CHICORY_CUT = registerSimpleItem("chicory_cut");
    public static final DeferredItem<Item> COMFREY_CUT = registerSimpleItem("comfrey_cut");
    public static final DeferredItem<Item> EYEBRIGHT_CUT = registerSimpleItem("eyebright_cut");
    public static final DeferredItem<Item> SAGE_CUT = registerSimpleItem("sage_cut");
    public static final DeferredItem<Item> CALENDULA_CUT = registerSimpleItem("calendula_cut");
    public static final DeferredItem<Item> WOOD_SORREL_RED_CUT = registerSimpleItem("wood_sorrel_red_cut");
    public static final DeferredItem<Item> WOOD_SORREL_YELLOW_CUT = registerSimpleItem("wood_sorrel_yellow_cut");
    public static final DeferredItem<Item> YARROW_CUT = registerSimpleItem("yarrow_cut");
    public static final DeferredItem<Item> BELL_FLOWER_CUT = registerSimpleItem("bell_flower_cut");
    public static final DeferredItem<Item> VALERIAN_WHITE_CUT = registerSimpleItem("valerian_white_cut");
    public static final DeferredItem<Item> VALERIAN_PINK_CUT = registerSimpleItem("valerian_pink_cut");
    public static final DeferredItem<Item> VALERIAN_RED_CUT = registerSimpleItem("valerian_red_cut");
    public static final DeferredItem<Item> CROWS_EYE_BERRIES = registerSimpleItem("crows_eye_berries");
    public static final DeferredItem<Item> THISTLE_CUT = registerSimpleItem("thistle_cut");
    public static final DeferredItem<Item> MINT_CUT = registerSimpleItem("mint_cut");
    public static final DeferredItem<Item> BELLADONNA_BERRIES = registerSimpleItem("belladonna_berries");
    public static final DeferredItem<Item> CATTAIL_FLUFF = registerSimpleItem("cattail_fluff");
    public static final DeferredItem<Item> SUGARCANE_PEEL = registerSimpleItem("sugarcane_peel");
    public static final DeferredItem<Item> SUGAR_CANE_PULP = registerSimpleItem("sugar_cane_pulp");
    public static final DeferredItem<Item> PLUM_UNRIPE = registerSimpleItem("plum_unripe");
    public static final DeferredItem<Item> PLUM_RIPE = ITEMS.register("plum_ripe", () -> new Item(new Item.Properties().food(RIPE_PLUM_FOOD)));
    public static final DeferredItem<Item> POISON_BOTTLE = registerSimpleItem("poison_bottle");
    public static final DeferredItem<Item> HERBAL_POWDER = registerSimpleItem("herbal_powder");

    public static final List<DeferredItem<BlockItem>> PLANT_ITEMS = List.of(
            WORMWOOD_ITEM,
            NETTLE_ITEM,
            HENBANE_ITEM,
            ST_JOHNS_WORT_ITEM,
            DATURA_ITEM,
            FIREWEED_ITEM,
            CHICORY_ITEM,
            CALAMUS_ITEM,
            CATTAIL_ITEM,
            CALENDULA_ITEM,
            COMFREY_ITEM,
            EYEBRIGHT_ITEM,
            SAGE_ITEM,
            WOOD_SORREL_RED_ITEM,
            WOOD_SORREL_YELLOW_ITEM,
            YARROW_WHITE_ITEM,
            YARROW_RED_ITEM,
            YARROW_PINK_ITEM,
            YARROW_YELLOW_ITEM,
            LYCORIS_ITEM,
            BELL_FLOWER_BLUE_ITEM,
            BELL_FLOWER_PINK_ITEM,
            BELL_FLOWER_VIOLET_ITEM,
            BELL_FLOWER_WHITE_ITEM,
            VALERIAN_WHITE_ITEM,
            VALERIAN_PINK_ITEM,
            VALERIAN_RED_ITEM,
            CROWS_EYE_ITEM,
            THISTLE_ITEM,
            MINT_ITEM,
            BELLADONNA_ITEM);

    public static final List<DeferredItem<BlockItem>> BLOCK_ITEMS = List.of(
            PLUM_PLANKS_ITEM,
            PLUM_SLAB_ITEM,
            PLUM_STAIRS_ITEM,
            PLUM_BUTTON_ITEM,
            PLUM_PRESSURE_PLATE_ITEM,
            PLUM_FENCE_ITEM,
            PLUM_FENCE_GATE_ITEM,
            PLUM_TRAPDOOR_ITEM,
            PLUM_LOG_ITEM,
            STRIPPED_PLUM_LOG_ITEM,
            PLUM_LEAVES_ITEM,
            PLUM_LEAVES_1_ITEM,
            PLUM_LEAVES_2_ITEM,
            PLUM_SAPLING_ITEM,
            MORTAR_ITEM);

    public static final List<DeferredItem<Item>> CUT_PLANT_ITEMS = List.of(
            WORMWOOD_CUT,
            NETTLE_CUT,
            HENBANE_CUT,
            ST_JOHNS_WORT_CUT,
            DATURA_CUT,
            FIREWEED_CUT,
            CHICORY_CUT,
            COMFREY_CUT,
            EYEBRIGHT_CUT,
            SAGE_CUT,
            CALENDULA_CUT,
            WOOD_SORREL_RED_CUT,
            WOOD_SORREL_YELLOW_CUT,
            YARROW_CUT,
            BELL_FLOWER_CUT,
            VALERIAN_WHITE_CUT,
            VALERIAN_PINK_CUT,
            VALERIAN_RED_CUT,
            CROWS_EYE_BERRIES,
            THISTLE_CUT,
            MINT_CUT,
            BELLADONNA_BERRIES,
            CATTAIL_FLUFF,
            SUGARCANE_PEEL,
            SUGAR_CANE_PULP,
            PLUM_UNRIPE,
            PLUM_RIPE,
            POISON_BOTTLE,
            HERBAL_POWDER);

    private ModItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static DeferredItem<BlockItem> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.registerSimpleBlockItem(name, block);
    }

    private static DeferredItem<Item> registerSimpleItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }
}
