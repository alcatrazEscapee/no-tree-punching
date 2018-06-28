/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

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

