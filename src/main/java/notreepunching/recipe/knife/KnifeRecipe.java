/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

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
