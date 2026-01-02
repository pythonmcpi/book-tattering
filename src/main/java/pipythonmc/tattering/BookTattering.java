package pipythonmc.tattering;

import net.fabricmc.api.ModInitializer;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookTattering implements ModInitializer {
	public static final String MOD_ID = "book-tattering";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final RecipeSerializer<TatteringRecipe> BOOT_TATTERING_RECIPE_SERIALIZER = RecipeSerializer.register(
            "book_tattering",
            new SpecialRecipeSerializer<>(TatteringRecipe::new)
    );

	@Override
	public void onInitialize() {
		LOGGER.info("Ready to tatter books!");
	}
}