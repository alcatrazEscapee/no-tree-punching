package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemSaw extends ItemAxe {

    public String name;

    ItemSaw(ToolMaterial material, String name){
        super(material,material.getAttackDamage(),-3.0F);

        this.name = name;
        register();

        setNoRepair();
        setMaxStackSize(1);
        setContainerItem(this);
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.TOOLS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack){
        ItemStack copy = stack.copy();
        copy.setItemDamage(copy.getItemDamage()+1);
        return copy;
    }
}
