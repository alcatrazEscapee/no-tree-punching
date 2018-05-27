package notreepunching.item;


import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

public class ItemGrindWheel extends ItemTool {

    public String name;

    ItemGrindWheel(String name){
        super(ToolMaterial.STONE, Sets.newHashSet());

        this.name = name;
        register();

        this.setNoRepair();
        this.setMaxDamage(25);
        this.setMaxStackSize(1);
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.ITEMS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

}

