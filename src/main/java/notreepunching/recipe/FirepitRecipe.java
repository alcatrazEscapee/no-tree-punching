package notreepunching.recipe;

import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class FirepitRecipe {

    private ItemStack output;
    private ItemStack ingredient;
    private int cookTime;

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

