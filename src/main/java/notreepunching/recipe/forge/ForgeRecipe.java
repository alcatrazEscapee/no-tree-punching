package notreepunching.recipe.forge;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class ForgeRecipe {

    private final ItemStack output;
    private final ItemStack inputStack;
    private String inputOre;
    private final int temperature;

    @ParametersAreNonnullByDefault
    public ForgeRecipe(ItemStack output, ItemStack input, int temp){
        this.output = output;
        this.inputStack = input;
        this.inputOre = "";
        this.temperature = temp;
    }

    public ForgeRecipe(@Nonnull ItemStack output, String oreInput, int temp){
        this(output, ItemStack.EMPTY, temp);
        this.inputOre = oreInput;
    }

    public ItemStack getOutput(){ return output; }

    public ItemStack getStack(){ return inputStack; }

    public String getOre(){ return inputOre; }

    public int getTemp(){ return temperature; }

}
