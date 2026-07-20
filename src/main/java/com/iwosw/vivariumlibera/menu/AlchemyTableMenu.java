package com.iwosw.vivariumlibera.menu;

import com.iwosw.vivariumlibera.block.entity.AlchemyLiquid;
import com.iwosw.vivariumlibera.block.entity.AlchemyTableBlockEntity;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class AlchemyTableMenu extends AbstractContainerMenu {
    public static final int DATA_LIT_TIME = 0;
    public static final int DATA_LIT_DURATION = 1;
    public static final int DATA_STRONG_FIRE = 2;
    public static final int DATA_BREW_PROGRESS = 3;
    public static final int DATA_BREW_TOTAL_TIME = 4;
    public static final int DATA_LIQUID = 5;
    public static final int DATA_LIQUID_PORTIONS = 6;
    public static final int DATA_HAS_CAULDRON = 7;
    public static final int DATA_COUNT = 8;

    // Menu slot indices (container slots BOOK/JUG stay physical on the table and are not shown here).
    private static final int INGREDIENT_START = 0;
    private static final int FUEL = 3;
    private static final int SAWDUST = 4;
    private static final int VIAL = 5;
    private static final int RESULT = 6;
    private static final int TABLE_SLOT_COUNT = 7;
    private static final int INV_START = TABLE_SLOT_COUNT;
    private static final int INV_END = INV_START + 27;
    private static final int HOTBAR_END = INV_END + 9;

    private final Container container;
    private final ContainerData data;
    private final BlockPos tablePos;

    public AlchemyTableMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory,
                new SimpleContainer(AlchemyTableBlockEntity.SLOT_COUNT),
                new SimpleContainerData(DATA_COUNT),
                buf.readBlockPos());
    }

    public AlchemyTableMenu(int containerId, Inventory playerInventory, Container container, ContainerData data, BlockPos tablePos) {
        super(ModMenuTypes.ALCHEMY_TABLE.get(), containerId);
        checkContainerSize(container, AlchemyTableBlockEntity.SLOT_COUNT);
        checkContainerDataCount(data, DATA_COUNT);
        this.container = container;
        this.data = data;
        this.tablePos = tablePos.immutable();

        for (int i = 0; i < AlchemyTableBlockEntity.INGREDIENT_SLOT_COUNT; i++) {
            addSlot(new Slot(container, AlchemyTableBlockEntity.INGREDIENT_SLOT_START + i, 58 + i * 18, 17));
        }
        addSlot(new Slot(container, AlchemyTableBlockEntity.FUEL_SLOT, 17, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return AlchemyTableBlockEntity.getBurnDuration(stack) > 0;
            }
        });
        addSlot(new Slot(container, AlchemyTableBlockEntity.SAWDUST_SLOT, 17, 17) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.SAWDUST.get());
            }
        });
        addSlot(new Slot(container, AlchemyTableBlockEntity.VIAL_SLOT, 112, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ModItems.VIAL.get());
            }
        });
        addSlot(new Slot(container, AlchemyTableBlockEntity.RESULT_SLOT, 140, 53) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < TABLE_SLOT_COUNT) {
            if (!moveItemStackTo(stack, INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            boolean moved = false;
            if (stack.is(ModItems.SAWDUST.get())) {
                moved = moveItemStackTo(stack, SAWDUST, SAWDUST + 1, false);
            } else if (stack.is(ModItems.VIAL.get())) {
                moved = moveItemStackTo(stack, VIAL, VIAL + 1, false);
            } else if (AlchemyTableBlockEntity.getBurnDuration(stack) > 0) {
                moved = moveItemStackTo(stack, FUEL, FUEL + 1, false);
            }
            if (!moved) {
                moved = moveItemStackTo(stack, INGREDIENT_START, INGREDIENT_START + AlchemyTableBlockEntity.INGREDIENT_SLOT_COUNT, false);
            }
            if (!moved) {
                if (index < INV_END) {
                    moved = moveItemStackTo(stack, INV_END, HOTBAR_END, false);
                } else {
                    moved = moveItemStackTo(stack, INV_START, INV_END, false);
                }
            }
            if (!moved) {
                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    public BlockPos getTablePos() {
        return tablePos;
    }

    public boolean isLit() {
        return data.get(DATA_LIT_TIME) > 0;
    }

    public boolean isStrongFire() {
        return isLit() && data.get(DATA_STRONG_FIRE) != 0;
    }

    /** Remaining fuel charge scaled to {@code height} pixels. */
    public int getLitScaled(int height) {
        int duration = data.get(DATA_LIT_DURATION);
        if (duration == 0) {
            return 0;
        }
        return Math.min(height, data.get(DATA_LIT_TIME) * height / duration);
    }

    /** Brew progress scaled to {@code width} pixels. */
    public int getBrewScaled(int width) {
        int total = data.get(DATA_BREW_TOTAL_TIME);
        int progress = data.get(DATA_BREW_PROGRESS);
        if (total == 0 || progress == 0) {
            return 0;
        }
        return progress * width / total;
    }

    public AlchemyLiquid getLiquid() {
        int index = data.get(DATA_LIQUID);
        AlchemyLiquid[] values = AlchemyLiquid.values();
        return index >= 0 && index < values.length ? values[index] : AlchemyLiquid.NONE;
    }

    public int getLiquidPortions() {
        return data.get(DATA_LIQUID_PORTIONS);
    }

    public boolean hasCauldron() {
        return data.get(DATA_HAS_CAULDRON) != 0;
    }
}
