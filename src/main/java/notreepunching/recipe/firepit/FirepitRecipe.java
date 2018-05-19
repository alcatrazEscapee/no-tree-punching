package notreepunching.recipe.firepit;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class FirepitRecipe {

    private final ItemStack output;
    private final ItemStack ingredient;

    public FirepitRecipe(ItemStack ingredient, ItemStack output){

        this.output = output;
        this.ingredient = ingredient;
    }

    @Nonnull
    public ItemStack getInput(){
        return ingredient;
    }

    @Nonnull
    public ItemStack getOutput(){
        return output;
    }

    public int getCount() { return output.getCount(); }

}

