package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemCrudeAxe extends ItemAxe {

    public String name;

    ItemCrudeAxe(ToolMaterial material, String name){
        super(material,4.0F,-3.0F);

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
        if(copy.getItemDamage() == copy.getMaxDamage()){
            copy = ItemStack.EMPTY;
        }
        return copy;
    }

}
