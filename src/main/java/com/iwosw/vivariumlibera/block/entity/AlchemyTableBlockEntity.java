package com.iwosw.vivariumlibera.block.entity;

import com.iwosw.vivariumlibera.menu.AlchemyTableMenu;
import com.iwosw.vivariumlibera.recipe.DecoctionRecipe;
import com.iwosw.vivariumlibera.recipe.DecoctionRecipeInput;
import com.iwosw.vivariumlibera.registry.ModBlockEntities;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModRecipes;
import com.iwosw.vivariumlibera.block.AlchemyTableBlock;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public final class AlchemyTableBlockEntity extends BlockEntity implements Container, MenuProvider {
    public static final int BOOK_SLOT = 0;
    public static final int JUG_SLOT = 1;
    public static final int INGREDIENT_SLOT_START = 2;
    public static final int INGREDIENT_SLOT_COUNT = 3;
    public static final int FUEL_SLOT = 5;
    public static final int SAWDUST_SLOT = 6;
    public static final int VIAL_SLOT = 7;
    public static final int RESULT_SLOT = 8;
    public static final int SLOT_COUNT = 9;

    public static final int PORTIONS_PER_JUG = 3;
    public static final int DEFAULT_BREW_TIME = 400;

    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    private boolean hasCauldron;
    private AlchemyLiquid liquid = AlchemyLiquid.NONE;
    private int liquidPortions;
    private int litTime;
    private int litDuration;
    private boolean strongFire;
    private int brewProgress;
    private int brewTotalTime = DEFAULT_BREW_TIME;
    /** Recipe id the current brew progress belongs to; progress resets when it changes. */
    @Nullable
    private String activeRecipeId;

    private final RecipeManager.CachedCheck<DecoctionRecipeInput, DecoctionRecipe> quickCheck =
            RecipeManager.createCheck(ModRecipes.DECOCTION_TYPE.get());

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case AlchemyTableMenu.DATA_LIT_TIME -> litTime;
                case AlchemyTableMenu.DATA_LIT_DURATION -> litDuration;
                case AlchemyTableMenu.DATA_STRONG_FIRE -> strongFire ? 1 : 0;
                case AlchemyTableMenu.DATA_BREW_PROGRESS -> brewProgress;
                case AlchemyTableMenu.DATA_BREW_TOTAL_TIME -> brewTotalTime;
                case AlchemyTableMenu.DATA_LIQUID -> liquid.ordinal();
                case AlchemyTableMenu.DATA_LIQUID_PORTIONS -> liquidPortions;
                case AlchemyTableMenu.DATA_HAS_CAULDRON -> hasCauldron ? 1 : 0;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case AlchemyTableMenu.DATA_LIT_TIME -> litTime = value;
                case AlchemyTableMenu.DATA_LIT_DURATION -> litDuration = value;
                case AlchemyTableMenu.DATA_STRONG_FIRE -> strongFire = value != 0;
                case AlchemyTableMenu.DATA_BREW_PROGRESS -> brewProgress = value;
                case AlchemyTableMenu.DATA_BREW_TOTAL_TIME -> brewTotalTime = value;
                case AlchemyTableMenu.DATA_LIQUID -> liquid = AlchemyLiquid.values()[Math.floorMod(value, AlchemyLiquid.values().length)];
                case AlchemyTableMenu.DATA_LIQUID_PORTIONS -> liquidPortions = value;
                case AlchemyTableMenu.DATA_HAS_CAULDRON -> hasCauldron = value != 0;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return AlchemyTableMenu.DATA_COUNT;
        }
    };

    public AlchemyTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_TABLE.get(), pos, state);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null) {
            AlchemyTableBlock.schedulePartRefresh(level, worldPosition);
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyTableBlockEntity table) {
        boolean wasLit = table.isLit();
        boolean changed = false;

        if (table.isLit()) {
            table.litTime--;
            if (table.litTime <= 0) {
                if (table.canBrew() && table.consumeFuel()) {
                    changed = true;
                } else {
                    table.strongFire = false;
                    table.litDuration = 0;
                    changed = true;
                }
            }
        }

        ItemStack sawdust = table.items.get(SAWDUST_SLOT);
        if (sawdust.is(ModItems.SAWDUST.get()) && table.strengthenFire()) {
            sawdust.shrink(1);
            changed = true;
        }

        // Fire out: pause (keep progress and active recipe). Lit: match a recipe and brew.
        if (table.isLit()) {
            RecipeHolder<DecoctionRecipe> recipe = table.findRecipe();
            if (recipe == null) {
                // No matching recipe (ingredients/liquid gone or wrong combo): reset.
                if (table.brewProgress > 0) {
                    table.brewProgress = 0;
                    changed = true;
                }
                table.activeRecipeId = null;
            } else {
                String recipeId = recipe.id().toString();
                if (!recipeId.equals(table.activeRecipeId)) {
                    table.activeRecipeId = recipeId;
                    table.brewProgress = 0;
                    table.brewTotalTime = recipe.value().cookTime();
                    changed = true;
                }
                // No empty vial or result slot occupied: pause without losing progress.
                if (table.canOutput()) {
                    table.brewProgress++;
                    if (table.brewProgress >= table.brewTotalTime) {
                        table.finishBrew(recipe.value());
                    }
                    changed = true;
                }
            }
        }

        if (wasLit != table.isLit()) {
            level.setBlock(pos, state.setValue(AlchemyTableBlock.LIT, table.isLit()), Block.UPDATE_ALL);
        }
        if (changed) {
            table.setChanged();
        }
    }

    // --- Fire ---

    public boolean isLit() {
        return litTime > 0;
    }

    public boolean isStrongFire() {
        return isLit() && strongFire;
    }

    public int getLitTime() {
        return litTime;
    }

    public int getLitDuration() {
        return litDuration;
    }

    public boolean canIgnite() {
        return !isLit() && getBurnDuration(items.get(FUEL_SLOT)) > 0;
    }

    public boolean ignite() {
        if (!canIgnite()) {
            return false;
        }
        boolean lit = consumeFuel();
        if (lit) {
            sync();
        }
        return lit;
    }

    /** Turns the current fuel charge into a strong (blue) flame. Lasts until the fire goes out. */
    public boolean strengthenFire() {
        if (!isLit() || strongFire) {
            return false;
        }
        strongFire = true;
        sync();
        return true;
    }

    private boolean consumeFuel() {
        ItemStack fuel = items.get(FUEL_SLOT);
        int duration = getBurnDuration(fuel);
        if (duration <= 0) {
            return false;
        }
        ItemStack remainder = fuel.getCraftingRemainingItem();
        fuel.shrink(1);
        if (fuel.isEmpty() && !remainder.isEmpty()) {
            items.set(FUEL_SLOT, remainder);
        }
        litTime = duration;
        litDuration = duration;
        return true;
    }

    public static int getBurnDuration(ItemStack stack) {
        return stack.isEmpty() ? 0 : stack.getBurnTime(RecipeType.SMELTING);
    }

    public boolean insertFuel(ItemStack stack) {
        if (getBurnDuration(stack) <= 0) {
            return false;
        }
        ItemStack current = items.get(FUEL_SLOT);
        if (current.isEmpty()) {
            items.set(FUEL_SLOT, stack.copyWithCount(1));
        } else if (ItemStack.isSameItemSameComponents(current, stack) && current.getCount() < current.getMaxStackSize()) {
            current.grow(1);
        } else {
            return false;
        }
        sync();
        return true;
    }

    // --- Cauldron (physical, rendered over the hearth) ---

    public boolean hasCauldron() {
        return hasCauldron;
    }

    public boolean placeCauldron() {
        if (hasCauldron) {
            return false;
        }
        hasCauldron = true;
        sync();
        return true;
    }

    /** Removes the cauldron. Fails while it still holds liquid. */
    public boolean removeCauldron() {
        if (!hasCauldron || hasLiquid()) {
            return false;
        }
        hasCauldron = false;
        sync();
        return true;
    }

    // --- Liquid ---

    public AlchemyLiquid getLiquid() {
        return liquid;
    }

    public int getLiquidPortions() {
        return liquidPortions;
    }

    public boolean hasLiquid() {
        return liquid != AlchemyLiquid.NONE && liquidPortions > 0;
    }

    /**
     * Pours a full jug into the cauldron. Returns the emptied jug to give back,
     * or {@link ItemStack#EMPTY} if this stack is not a pourable full jug or the cauldron is occupied.
     */
    public ItemStack tryFillFromJug(ItemStack stack) {
        AlchemyLiquid poured = liquidFromJug(stack);
        ItemStack emptied = emptyJugFor(stack);
        if (poured == AlchemyLiquid.NONE || emptied.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (!hasCauldron || hasLiquid()) {
            return ItemStack.EMPTY;
        }
        liquid = poured;
        liquidPortions = PORTIONS_PER_JUG;
        sync();
        return emptied;
    }

    private static AlchemyLiquid liquidFromJug(ItemStack stack) {
        if (stack.is(ModItems.WATER_JUG_FULL.get())) {
            return AlchemyLiquid.WATER;
        }
        if (stack.is(ModItems.OIL_JUG_FULL.get())) {
            return AlchemyLiquid.OIL;
        }
        if (stack.is(ModItems.WINE_JUG_FULL.get())) {
            return AlchemyLiquid.WINE;
        }
        return AlchemyLiquid.NONE;
    }

    private static ItemStack emptyJugFor(ItemStack stack) {
        if (stack.is(ModItems.WATER_JUG_FULL.get())) {
            return new ItemStack(ModItems.WATER_JUG_EMPTY.get());
        }
        if (stack.is(ModItems.OIL_JUG_FULL.get())) {
            return new ItemStack(ModItems.OIL_JUG_EMPTY.get());
        }
        if (stack.is(ModItems.WINE_JUG_FULL.get())) {
            return new ItemStack(ModItems.WINE_JUG_EMPTY.get());
        }
        return ItemStack.EMPTY;
    }

    // --- Brewing ---

    public int getBrewProgress() {
        return brewProgress;
    }

    public int getBrewTotalTime() {
        return brewTotalTime;
    }

    public boolean canBrew() {
        return hasCauldron && hasLiquid() && hasIngredients();
    }

    private boolean hasIngredients() {
        for (int i = 0; i < INGREDIENT_SLOT_COUNT; i++) {
            if (!items.get(INGREDIENT_SLOT_START + i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    private RecipeHolder<DecoctionRecipe> findRecipe() {
        if (level == null || !canBrew()) {
            return null;
        }
        DecoctionRecipeInput input = new DecoctionRecipeInput(
                List.of(
                        items.get(INGREDIENT_SLOT_START),
                        items.get(INGREDIENT_SLOT_START + 1),
                        items.get(INGREDIENT_SLOT_START + 2)),
                liquid,
                isStrongFire());
        return quickCheck.getRecipeFor(input, level).orElse(null);
    }

    /** An empty vial is ready and the result slot is free. */
    private boolean canOutput() {
        return items.get(VIAL_SLOT).is(ModItems.VIAL.get()) && items.get(RESULT_SLOT).isEmpty();
    }

    private void finishBrew(DecoctionRecipe recipe) {
        for (int i = 0; i < INGREDIENT_SLOT_COUNT; i++) {
            ItemStack ingredient = items.get(INGREDIENT_SLOT_START + i);
            if (!ingredient.isEmpty()) {
                ingredient.shrink(1);
            }
        }
        items.get(VIAL_SLOT).shrink(1);
        items.set(RESULT_SLOT, recipe.createResult());
        liquidPortions--;
        if (liquidPortions <= 0) {
            liquidPortions = 0;
            liquid = AlchemyLiquid.NONE;
        }
        brewProgress = 0;
        activeRecipeId = null;
        if (level != null) {
            level.playSound(null, worldPosition, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        sync();
    }

    // --- Book / jug (physical slots rendered on the table) ---

    public ItemStack getStoredBook() {
        return items.get(BOOK_SLOT);
    }

    public boolean hasBook() {
        return !getStoredBook().isEmpty();
    }

    public boolean placeBook(ItemStack stack) {
        if (stack.isEmpty() || hasBook()) {
            return false;
        }

        items.set(BOOK_SLOT, stack.copyWithCount(1));
        sync();
        return true;
    }

    public ItemStack getStoredJug() {
        return items.get(JUG_SLOT);
    }

    public boolean hasJug() {
        return !getStoredJug().isEmpty();
    }

    public boolean placeJug(ItemStack stack) {
        if (stack.isEmpty() || hasJug()) {
            return false;
        }

        items.set(JUG_SLOT, stack.copyWithCount(1));
        sync();
        return true;
    }

    public ItemStack removeJug() {
        ItemStack jug = getStoredJug();
        if (jug.isEmpty()) {
            return ItemStack.EMPTY;
        }

        items.set(JUG_SLOT, ItemStack.EMPTY);
        sync();
        return jug;
    }

    public ItemStack removeBook() {
        ItemStack book = getStoredBook();
        if (book.isEmpty()) {
            return ItemStack.EMPTY;
        }

        items.set(BOOK_SLOT, ItemStack.EMPTY);
        sync();
        return book;
    }

    public void dropContents(Level level, BlockPos pos) {
        if (level.isClientSide()) {
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (!stack.isEmpty()) {
                Block.popResource(level, pos, stack);
                items.set(i, ItemStack.EMPTY);
            }
        }
        if (hasCauldron) {
            hasCauldron = false;
            Block.popResource(level, pos, new ItemStack(ModItems.ALCHEMY_CAULDRON.get()));
        }
        sync();
    }

    // --- Container ---

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        stack.limitSize(getMaxStackSize(stack));
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    // --- MenuProvider ---

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.vivariumlibera.alchemy_table.title");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new AlchemyTableMenu(containerId, playerInventory, this, dataAccess, worldPosition);
    }

    // --- Persistence / sync ---

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        ContainerHelper.saveAllItems(tag, items, registries);
        tag.putBoolean("HasCauldron", hasCauldron);
        tag.putString("Liquid", liquid.getSerializedName());
        tag.putInt("LiquidPortions", liquidPortions);
        tag.putInt("LitTime", litTime);
        tag.putInt("LitDuration", litDuration);
        tag.putBoolean("StrongFire", strongFire);
        tag.putInt("BrewProgress", brewProgress);
        tag.putInt("BrewTotalTime", brewTotalTime);
        if (activeRecipeId != null) {
            tag.putString("ActiveRecipe", activeRecipeId);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items.clear();
        ContainerHelper.loadAllItems(tag, items, registries);
        liquid = AlchemyLiquid.byName(tag.getString("Liquid"));
        liquidPortions = tag.getInt("LiquidPortions");
        // Legacy tables (saved before the cauldron item existed) keep their liquid usable.
        hasCauldron = tag.contains("HasCauldron")
                ? tag.getBoolean("HasCauldron")
                : liquid != AlchemyLiquid.NONE && liquidPortions > 0;
        litTime = tag.getInt("LitTime");
        litDuration = tag.getInt("LitDuration");
        strongFire = tag.getBoolean("StrongFire");
        brewProgress = tag.getInt("BrewProgress");
        brewTotalTime = tag.contains("BrewTotalTime") ? tag.getInt("BrewTotalTime") : DEFAULT_BREW_TIME;
        activeRecipeId = tag.contains("ActiveRecipe") ? tag.getString("ActiveRecipe") : null;
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void sync() {
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }
}
