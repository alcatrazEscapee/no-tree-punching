package notreepunching.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

public class ItemBase extends Item {

    public String name;

    public ItemBase(String name){
        super();

        this.name = name;
        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.ITEMS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }
}
