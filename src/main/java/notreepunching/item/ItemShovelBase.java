package notreepunching.item;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;

public class ItemShovelBase extends ItemSpade {

    public String name;

    public ItemShovelBase(ToolMaterial material, String name){
        super(material);

        this.name = name;
        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name,true);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }
}
