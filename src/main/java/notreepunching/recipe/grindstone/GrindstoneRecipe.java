/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.recipe.grindstone;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GrindstoneRecipe {

    private final ItemStack output;
    private final ItemStack input;
    private String oreInput;
    private int amountIn;

    public GrindstoneRecipe(ItemStack output, ItemStack input){
        this.output = output;
        this.input = input;
        this.oreInput = "";
        this.amountIn = input.getCount();
    }

    public GrindstoneRecipe(ItemStack output, String oreInput, int amountIn){
        this(output, ItemStack.EMPTY);
        this.oreInput = oreInput;
        this.amountIn = amountIn;
    }

    public ItemStack getOutput(){ return output; }

    public ItemStack getInput(){ return input; }

    public String getOreInput(){ return oreInput; }

    public int getInputCount(){ return amountIn; }
}
