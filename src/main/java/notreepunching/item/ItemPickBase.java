package notreepunching.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

import java.util.Set;

public class ItemPickBase extends ItemPickaxe {

    public String name;

    public ItemPickBase(ToolMaterial material, String name){

        super(material);
        this.name = name;
        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.TOOLS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

}
