package com.iwosw.vivariumlibera.registry;

import com.iwosw.vivariumlibera.VivariumLibera;
import com.iwosw.vivariumlibera.block.entity.MortarBlockEntity;
import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import com.iwosw.vivariumlibera.block.entity.AlchemyTablePartBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(
            Registries.BLOCK_ENTITY_TYPE,
            VivariumLibera.MOD_ID);

    public static final Supplier<BlockEntityType<MortarBlockEntity>> MORTAR = BLOCK_ENTITY_TYPES.register(
            "mortar",
            () -> BlockEntityType.Builder.of(MortarBlockEntity::new, ModBlocks.MORTAR.get()).build(null));

    public static final Supplier<BlockEntityType<AlchemyTableBlockEntity>> ALCHEMY_TABLE = BLOCK_ENTITY_TYPES.register(
            "alchemy_table",
            () -> BlockEntityType.Builder.of(AlchemyTableBlockEntity::new, ModBlocks.ALCHEMY_TABLE.get()).build(null));

    public static final Supplier<BlockEntityType<AlchemyTablePartBlockEntity>> ALCHEMY_TABLE_PART = BLOCK_ENTITY_TYPES.register(
            "alchemy_table_part",
            () -> BlockEntityType.Builder.of(AlchemyTablePartBlockEntity::new, ModBlocks.ALCHEMY_TABLE_PART.get()).build(null));

    private ModBlockEntities() {
    }

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
