package notreepunching.recipe.knife;

import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class KnifeRecipe {

    private List<ItemStack> drops;
    private ItemStack ingredient;

    public KnifeRecipe(ItemStack ingredient, ItemStack... drops){

        this.drops = Arrays.asList(drops);
        this.ingredient = ingredient;
    }

    public ItemStack getInput(){
        return ingredient;
    }

    public List<ItemStack> getOutput(){
        return drops;
    }

}
