package com.iwosw.vivariumlibera.registry;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.block.AquaticSimplePlantBlock;
import com.iwosw.vivariumlibera.block.AquaticTallPlantBlock;
import com.iwosw.vivariumlibera.block.PlumFenceBlock;
import com.iwosw.vivariumlibera.block.PlumSaplingBlock;
import com.iwosw.vivariumlibera.block.RegrowingPetalsBlock;
import com.iwosw.vivariumlibera.block.SimplePlantBlock;
import com.iwosw.vivariumlibera.block.StackablePlantBlock;
import com.iwosw.vivariumlibera.block.TallPlantBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(VivariumLibera.MOD_ID);

    public static final DeferredBlock<StackablePlantBlock> WORMWOOD = registerStackablePlant("wormwood");
    public static final DeferredBlock<StackablePlantBlock> NETTLE = registerMoistStackablePlant("nettle");
    public static final DeferredBlock<StackablePlantBlock> HENBANE = registerStackablePlant("henbane");
    public static final DeferredBlock<StackablePlantBlock> ST_JOHNS_WORT = registerStackablePlant("st_johns_wort");
    public static final DeferredBlock<StackablePlantBlock> DATURA = registerStackablePlant("datura");
    public static final DeferredBlock<TallPlantBlock> FIREWEED = registerTallPlant("fireweed");
    public static final DeferredBlock<TallPlantBlock> CHICORY = registerTallPlant("chicory");

    public static final DeferredBlock<AquaticSimplePlantBlock> CALAMUS = registerAquaticSimplePlant("calamus");
    public static final DeferredBlock<AquaticTallPlantBlock> CATTAIL = registerAquaticTallPlant("cattail");
    public static final DeferredBlock<SimplePlantBlock> CALENDULA = registerSimplePlant("calendula");
    public static final DeferredBlock<SimplePlantBlock> COMFREY = registerMoistSimplePlant("comfrey");
    public static final DeferredBlock<SimplePlantBlock> EYEBRIGHT = registerSimplePlant("eyebright");
    public static final DeferredBlock<SimplePlantBlock> SAGE = registerSimplePlant("sage");
    public static final DeferredBlock<RegrowingPetalsBlock> WOOD_SORREL_RED = registerMeadowFlower("wood_sorrel_red");
    public static final DeferredBlock<RegrowingPetalsBlock> WOOD_SORREL_YELLOW = registerMeadowFlower("wood_sorrel_yellow");
    public static final DeferredBlock<SimplePlantBlock> YARROW_WHITE = registerSimplePlant("yarrow_white");
    public static final DeferredBlock<SimplePlantBlock> YARROW_RED = registerSimplePlant("yarrow_red");
    public static final DeferredBlock<SimplePlantBlock> YARROW_PINK = registerSimplePlant("yarrow_pink");
    public static final DeferredBlock<SimplePlantBlock> YARROW_YELLOW = registerSimplePlant("yarrow_yellow");
    public static final DeferredBlock<SimplePlantBlock> LYCORIS = registerSimplePlant("lycoris");
    public static final DeferredBlock<TallPlantBlock> BELL_FLOWER_BLUE = registerTallPlant("bell_flower_blue");
    public static final DeferredBlock<TallPlantBlock> BELL_FLOWER_PINK = registerTallPlant("bell_flower_pink");
    public static final DeferredBlock<TallPlantBlock> BELL_FLOWER_VIOLET = registerTallPlant("bell_flower_violet");
    public static final DeferredBlock<TallPlantBlock> BELL_FLOWER_WHITE = registerTallPlant("bell_flower_white");
    public static final DeferredBlock<TallPlantBlock> VALERIAN_WHITE = registerTallPlant("valerian_white");
    public static final DeferredBlock<TallPlantBlock> VALERIAN_PINK = registerTallPlant("valerian_pink");
    public static final DeferredBlock<TallPlantBlock> VALERIAN_RED = registerTallPlant("valerian_red");
    public static final DeferredBlock<SimplePlantBlock> CROWS_EYE = registerMoistSimplePlant("crows_eye");
    public static final DeferredBlock<SimplePlantBlock> THISTLE = registerSimplePlant("thistle");
    public static final DeferredBlock<SimplePlantBlock> MINT = registerMoistSimplePlant("mint");
    public static final DeferredBlock<TallPlantBlock> BELLADONNA = registerTallPlant("belladonna");

    public static final DeferredBlock<Block> PLUM_PLANKS = registerPlanks("plum_planks");
    public static final DeferredBlock<SlabBlock> PLUM_SLAB = registerSlab("plum_slab");
    public static final DeferredBlock<StairBlock> PLUM_STAIRS = registerStairs("plum_stairs");
    public static final DeferredBlock<ButtonBlock> PLUM_BUTTON = registerButton("plum_button");
    public static final DeferredBlock<PressurePlateBlock> PLUM_PRESSURE_PLATE = registerPressurePlate("plum_pressure_plate");
    public static final DeferredBlock<PlumFenceBlock> PLUM_FENCE = registerFence("plum_fence");
    public static final DeferredBlock<FenceGateBlock> PLUM_FENCE_GATE = registerFenceGate("plum_fence_gate");
    public static final DeferredBlock<TrapDoorBlock> PLUM_TRAPDOOR = registerTrapdoor("plum_trapdoor");
    public static final DeferredBlock<RotatedPillarBlock> PLUM_LOG = registerLog("plum_log");
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PLUM_LOG = registerLog("stripped_plum_log");
    public static final DeferredBlock<LeavesBlock> PLUM_LEAVES = registerLeaves("plum_leaves");
    public static final DeferredBlock<LeavesBlock> PLUM_LEAVES_1 = registerLeaves("plum_leaves_1");
    public static final DeferredBlock<LeavesBlock> PLUM_LEAVES_2 = registerLeaves("plum_leaves_2");
    public static final DeferredBlock<SaplingBlock> PLUM_SAPLING = registerSapling("plum_sapling");

    private ModBlocks() {
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static DeferredBlock<StackablePlantBlock> registerStackablePlant(String name) {
        return BLOCKS.register(name, () -> new StackablePlantBlock(plantProperties()));
    }

    private static DeferredBlock<StackablePlantBlock> registerMoistStackablePlant(String name) {
        return BLOCKS.register(name, () -> new StackablePlantBlock(plantProperties(), true));
    }

    private static DeferredBlock<SimplePlantBlock> registerSimplePlant(String name) {
        return BLOCKS.register(name, () -> new SimplePlantBlock(plantProperties()));
    }

    private static DeferredBlock<SimplePlantBlock> registerMoistSimplePlant(String name) {
        return BLOCKS.register(name, () -> new SimplePlantBlock(plantProperties(), true));
    }

    private static DeferredBlock<AquaticSimplePlantBlock> registerAquaticSimplePlant(String name) {
        return BLOCKS.register(name, () -> new AquaticSimplePlantBlock(plantProperties()));
    }

    private static DeferredBlock<TallPlantBlock> registerTallPlant(String name) {
        return BLOCKS.register(name, () -> new TallPlantBlock(plantProperties()));
    }

    private static DeferredBlock<AquaticTallPlantBlock> registerAquaticTallPlant(String name) {
        return BLOCKS.register(name, () -> new AquaticTallPlantBlock(plantProperties()));
    }

    private static DeferredBlock<RegrowingPetalsBlock> registerMeadowFlower(String name) {
        return BLOCKS.register(name, () -> new RegrowingPetalsBlock(meadowFlowerProperties()));
    }

    private static DeferredBlock<Block> registerPlanks(String name) {
        return BLOCKS.register(name, () -> new Block(planksProperties()));
    }

    private static DeferredBlock<SlabBlock> registerSlab(String name) {
        return BLOCKS.register(name, () -> new SlabBlock(slabProperties()));
    }

    private static DeferredBlock<StairBlock> registerStairs(String name) {
        return BLOCKS.register(name, () -> new StairBlock(PLUM_PLANKS.get().defaultBlockState(), planksProperties()));
    }

    private static DeferredBlock<ButtonBlock> registerButton(String name) {
        return BLOCKS.register(name, () -> new ButtonBlock(BlockSetType.OAK, 30, buttonProperties()));
    }

    private static DeferredBlock<PressurePlateBlock> registerPressurePlate(String name) {
        return BLOCKS.register(name, () -> new PressurePlateBlock(BlockSetType.OAK, pressurePlateProperties()));
    }

    private static DeferredBlock<PlumFenceBlock> registerFence(String name) {
        return BLOCKS.register(name, () -> new PlumFenceBlock(fenceProperties()));
    }

    private static DeferredBlock<FenceGateBlock> registerFenceGate(String name) {
        return BLOCKS.register(name, () -> new FenceGateBlock(WoodType.OAK, fenceGateProperties()));
    }

    private static DeferredBlock<TrapDoorBlock> registerTrapdoor(String name) {
        return BLOCKS.register(name, () -> new TrapDoorBlock(BlockSetType.OAK, trapdoorProperties()));
    }

    private static DeferredBlock<RotatedPillarBlock> registerLog(String name) {
        return BLOCKS.register(name, () -> new RotatedPillarBlock(logProperties()));
    }

    private static DeferredBlock<LeavesBlock> registerLeaves(String name) {
        return BLOCKS.register(name, () -> new LeavesBlock(leavesProperties()));
    }

    private static DeferredBlock<SaplingBlock> registerSapling(String name) {
        return BLOCKS.register(name, () -> new PlumSaplingBlock(saplingProperties()));
    }

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.DANDELION);
    }

    private static BlockBehaviour.Properties meadowFlowerProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_PETALS);
    }

    private static BlockBehaviour.Properties planksProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS);
    }

    private static BlockBehaviour.Properties slabProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB);
    }

    private static BlockBehaviour.Properties buttonProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON);
    }

    private static BlockBehaviour.Properties pressurePlateProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE);
    }

    private static BlockBehaviour.Properties fenceProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE);
    }

    private static BlockBehaviour.Properties fenceGateProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE);
    }

    private static BlockBehaviour.Properties trapdoorProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR);
    }

    private static BlockBehaviour.Properties logProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG);
    }

    private static BlockBehaviour.Properties leavesProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES);
    }

    private static BlockBehaviour.Properties saplingProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING);
    }
}
