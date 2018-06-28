/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.recipe.forge;

import net.minecraft.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ForgeRecipe {

    private final ItemStack output;
    private final ItemStack inputStack;
    private String inputOre;
    private final int temperature;
    private int count;

    public ForgeRecipe(ItemStack output, ItemStack input, int temp){
        this.output = output;
        this.inputStack = input;
        this.inputOre = "";
        this.temperature = temp;
        this.count = input.getCount();
    }

    public ForgeRecipe(ItemStack output, String oreInput, int count, int temp){
        this(output, ItemStack.EMPTY, temp);
        this.inputOre = oreInput;
        this.count = count;
    }

    public ItemStack getOutput(){ return output; }

    public ItemStack getStack(){ return inputStack; }

    public String getOre(){ return inputOre; }

    public int getTemp(){ return temperature; }

    public int getCount(){ return inputOre.equals("") ? inputStack.getCount() : count; }

}
