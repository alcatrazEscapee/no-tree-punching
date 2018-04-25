package notreepunching.item;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;

public class ItemSaw extends ItemAxe {

    public String name;

    public ItemSaw(ToolMaterial material, String name){
        super(material,material.getAttackDamage(),-3.0F);

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
