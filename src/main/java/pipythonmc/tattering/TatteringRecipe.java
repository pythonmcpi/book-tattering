package pipythonmc.tattering;

import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

/**
 * Crafting recipe that takes a written book and shears, and makes the book tattered. Decrements shear durability.
 */
public class TatteringRecipe extends SpecialCraftingRecipe {
    public TatteringRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        List<ItemStack> stacks = inventory.getInputStacks();

        if (stacks.stream().filter(s -> !s.isEmpty()).count() != 2) return false;

        boolean hasBook = stacks.stream().anyMatch(s -> s.isOf(Items.WRITTEN_BOOK) && (s.getNbt() == null || s.getNbt().getInt("generation") != 3));
        boolean hasShears = stacks.stream().anyMatch(s -> s.isOf(Items.SHEARS));

        return hasBook && hasShears;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack book = inventory.getInputStacks()
                .stream().filter(s -> s.isOf(Items.WRITTEN_BOOK) && (s.getNbt() == null || s.getNbt().getInt("generation") != 3))
                .findFirst().orElse(ItemStack.EMPTY);

        if (book.isEmpty()) return book;

        ItemStack out = book.copy();
        out.setCount(1);
        out.getOrCreateNbt().putInt("generation", 3);

        return out;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
        DefaultedList<ItemStack> remainders = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for (int i = 0; i < remainders.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isOf(Items.SHEARS)) {
                ItemStack damaged = stack.copy();
                damaged.setDamage(damaged.getDamage() + 1);

                if (damaged.getDamage() < damaged.getMaxDamage()) {
                    remainders.set(i, damaged);
                }
            }
        }

        return remainders;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BookTattering.BOOT_TATTERING_RECIPE_SERIALIZER;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }
}
