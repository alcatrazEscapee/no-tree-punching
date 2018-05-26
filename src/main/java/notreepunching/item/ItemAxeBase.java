package notreepunching.item;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;

public class ItemAxeBase extends ItemAxe {

    public String name;

    public ItemAxeBase(ToolMaterial material, String name){
        super(material, material.getAttackDamage(), -3.0F);

        this.name = name;
        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name,true);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }
}
