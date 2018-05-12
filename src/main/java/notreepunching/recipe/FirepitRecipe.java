package notreepunching.recipe;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class FirepitRecipe {

    private final ItemStack output;
    private final ItemStack ingredient;
    private final int cookTime;

    public FirepitRecipe(ItemStack ingredient, int time, ItemStack output){

        this.output = output;
        this.ingredient = ingredient;
        this.cookTime = time;
    }

    public ItemStack getInput(){
        return ingredient;
    }

    public ItemStack getOutput(){
        return output;
    }

    public int getCookTime() { return cookTime; }

    public int getCount() { return output.getCount(); }

}

