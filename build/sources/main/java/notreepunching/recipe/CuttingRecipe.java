package notreepunching.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CuttingRecipe {

    public ItemStack[] drops;
    public ItemStack ingredient;

    public CuttingRecipe(Item ingredient, ItemStack[] drops){
        this.drops = drops;
        this.ingredient = new ItemStack(ingredient,1,0);
    }

    public Item getInputItem(){
        return ingredient.getItem();
    }
}
