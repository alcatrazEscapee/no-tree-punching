package notreepunching.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import notreepunching.NoTreePunching;
import notreepunching.recipe.ModRecipes;

import java.util.Set;

public class ItemSaw extends ItemAxe {

    public String name;

    public ItemSaw(ToolMaterial material, String name){
        super(material,4.0F,-3.0F);

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(NoTreePunching.NTP_Tab);

        setNoRepair();
        setMaxStackSize(1);
        setContainerItem(this);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack){
        ItemStack copy = stack.copy();
        copy.setItemDamage(copy.getItemDamage()+1);
        return copy;
    }
}
