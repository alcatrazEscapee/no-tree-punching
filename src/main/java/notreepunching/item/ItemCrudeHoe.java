package notreepunching.item;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;

public class ItemCrudeHoe extends ItemHoe {

    public String name;

    public ItemCrudeHoe(ToolMaterial material, String name){
        super(material);
        this.name = name;

        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this, name, true);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }
}
