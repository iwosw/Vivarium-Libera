package com.iwosw.vivariumlibera.registry;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.block.SimplePlantBlock;
import com.iwosw.vivariumlibera.block.StackablePlantBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(VivariumLibera.MOD_ID);

    public static final DeferredBlock<StackablePlantBlock> WORMWOOD = registerStackablePlant("wormwood");
    public static final DeferredBlock<StackablePlantBlock> NETTLE = registerStackablePlant("nettle");
    public static final DeferredBlock<StackablePlantBlock> HENBANE = registerStackablePlant("henbane");
    public static final DeferredBlock<StackablePlantBlock> ST_JOHNS_WORT = registerStackablePlant("st_johns_wort");
    public static final DeferredBlock<StackablePlantBlock> DATURA = registerStackablePlant("datura");
    public static final DeferredBlock<StackablePlantBlock> FIREWEED = registerStackablePlant("fireweed");
    public static final DeferredBlock<StackablePlantBlock> CHICORY = registerStackablePlant("chicory");

    public static final DeferredBlock<SimplePlantBlock> CALAMUS = registerSimplePlant("calamus");
    public static final DeferredBlock<SimplePlantBlock> CATTAIL = registerSimplePlant("cattail");
    public static final DeferredBlock<SimplePlantBlock> CALENDULA = registerSimplePlant("calendula");
    public static final DeferredBlock<PinkPetalsBlock> WOOD_SORREL_RED = registerMeadowFlower("wood_sorrel_red");
    public static final DeferredBlock<PinkPetalsBlock> WOOD_SORREL_YELLOW = registerMeadowFlower("wood_sorrel_yellow");
    public static final DeferredBlock<SimplePlantBlock> YARROW_WHITE = registerSimplePlant("yarrow_white");
    public static final DeferredBlock<SimplePlantBlock> YARROW_RED = registerSimplePlant("yarrow_red");
    public static final DeferredBlock<SimplePlantBlock> YARROW_PINK = registerSimplePlant("yarrow_pink");
    public static final DeferredBlock<SimplePlantBlock> YARROW_YELLOW = registerSimplePlant("yarrow_yellow");

    private ModBlocks() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static DeferredBlock<StackablePlantBlock> registerStackablePlant(String name) {
        return BLOCKS.register(name, () -> new StackablePlantBlock(plantProperties()));
    }

    private static DeferredBlock<SimplePlantBlock> registerSimplePlant(String name) {
        return BLOCKS.register(name, () -> new SimplePlantBlock(plantProperties()));
    }

    private static DeferredBlock<PinkPetalsBlock> registerMeadowFlower(String name) {
        return BLOCKS.register(name, () -> new PinkPetalsBlock(meadowFlowerProperties()));
    }

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION);
    }

    private static BlockBehaviour.Properties meadowFlowerProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS);
    }
}
