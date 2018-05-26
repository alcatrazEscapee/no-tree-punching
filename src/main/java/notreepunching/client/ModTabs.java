package notreepunching.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;

import javax.annotation.Nonnull;

public class ModTabs extends CreativeTabs {

    public static final ModTabs ITEMS_TAB = new ModTabs("items");
    public static final ModTabs TOOLS_TAB = new ModTabs("tools");

    private Item tabItem;

    public ModTabs(String label) {
        super(NoTreePunching.MODID+"."+label);
    }

    @Override
    @Nonnull
    public ItemStack getTabIconItem(){
        return new ItemStack(tabItem);
    }

    public void setTabItem(Item item){
        tabItem = item;
    }
}
