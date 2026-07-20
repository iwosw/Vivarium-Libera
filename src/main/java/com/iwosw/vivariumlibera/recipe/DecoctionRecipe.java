package com.iwosw.vivariumlibera.recipe;

import com.iwosw.vivariumlibera.block.entity.AlchemyLiquid;
import com.iwosw.vivariumlibera.item.DecoctionItem;
import com.iwosw.vivariumlibera.registry.ModItems;
import com.iwosw.vivariumlibera.registry.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * Alchemy table brewing recipe: 1-3 ingredients + a cauldron liquid
 * (optionally requiring strong fire) cooked into a named decoction.
 * Ingredient order does not matter.
 */
public final class DecoctionRecipe implements Recipe<DecoctionRecipeInput> {
    public static final int DEFAULT_COOK_TIME = 400;

    private final AlchemyLiquid liquid;
    private final boolean strongFire;
    private final List<Ingredient> ingredients;
    private final int cookTime;
    private final String result;

    public DecoctionRecipe(AlchemyLiquid liquid, boolean strongFire, List<Ingredient> ingredients, int cookTime, String result) {
        this.liquid = liquid;
        this.strongFire = strongFire;
        this.ingredients = List.copyOf(ingredients);
        this.cookTime = cookTime;
        this.result = result;
    }

    public AlchemyLiquid liquid() {
        return liquid;
    }

    public boolean requiresStrongFire() {
        return strongFire;
    }

    public int cookTime() {
        return cookTime;
    }

    public String result() {
        return result;
    }

    @Override
    public boolean matches(DecoctionRecipeInput input, Level level) {
        if (input.liquid() != liquid) {
            return false;
        }
        if (strongFire && !input.strongFire()) {
            return false;
        }
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }
        if (stacks.size() != ingredients.size()) {
            return false;
        }
        return matchesUnordered(stacks, 0, new boolean[ingredients.size()]);
    }

    /** Order-free matching: each stack must claim a distinct ingredient (backtracking, sizes <= 3). */
    private boolean matchesUnordered(List<ItemStack> stacks, int index, boolean[] used) {
        if (index == stacks.size()) {
            return true;
        }
        for (int i = 0; i < ingredients.size(); i++) {
            if (!used[i] && ingredients.get(i).test(stacks.get(index))) {
                used[i] = true;
                if (matchesUnordered(stacks, index + 1, used)) {
                    return true;
                }
                used[i] = false;
            }
        }
        return false;
    }

    @Override
    public ItemStack assemble(DecoctionRecipeInput input, HolderLookup.Provider registries) {
        return createResult();
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return createResult();
    }

    public ItemStack createResult() {
        return DecoctionItem.create(ModItems.DECOCTION.get(), result);
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingredients);
        return list;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DECOCTION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.DECOCTION_TYPE.get();
    }

    public static final class Serializer implements RecipeSerializer<DecoctionRecipe> {
        private static final MapCodec<DecoctionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                AlchemyLiquid.CODEC.fieldOf("liquid").forGetter(DecoctionRecipe::liquid),
                Codec.BOOL.optionalFieldOf("strong_fire", false).forGetter(DecoctionRecipe::requiresStrongFire),
                ExtraCodecs.nonEmptyList(Ingredient.CODEC_NONEMPTY.listOf()).fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                ExtraCodecs.POSITIVE_INT.optionalFieldOf("cook_time", DEFAULT_COOK_TIME).forGetter(DecoctionRecipe::cookTime),
                Codec.STRING.fieldOf("decoction").codec().fieldOf("result").forGetter(DecoctionRecipe::result)
        ).apply(instance, DecoctionRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, DecoctionRecipe> STREAM_CODEC = StreamCodec.composite(
                AlchemyLiquid.STREAM_CODEC, DecoctionRecipe::liquid,
                ByteBufCodecs.BOOL, DecoctionRecipe::requiresStrongFire,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), recipe -> recipe.ingredients,
                ByteBufCodecs.VAR_INT, DecoctionRecipe::cookTime,
                ByteBufCodecs.STRING_UTF8, DecoctionRecipe::result,
                DecoctionRecipe::new);

        @Override
        public MapCodec<DecoctionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DecoctionRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
