package notreepunching.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import notreepunching.NoTreePunching;

public class ItemRock extends ItemBase {

    public ItemRock(String name){
        super(name);

        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return super.getUnlocalizedName() + "_" + getStoneName(stack);
    }
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (getCreativeTab() != tab) return;
        //super.getSubItems(tab, items);
        for (int i = 0; i < 7; ++i) {
            if(!NoTreePunching.replaceQuarkStones && (i == 4 || i == 5)) { continue; }
            if(!NoTreePunching.replaceRusticStone && (i == 6)) { continue; }
            items.add(new ItemStack(this, 1, i));
        }
    }

    public String getStoneName(ItemStack stack){
        switch(stack.getMetadata()){
            case 0:
                return "stone"; // Vanilla Stone
            case 1:
                return "andesite"; // Vanilla Stone Variants
            case 2:
                return "diorite";
            case 3:
                return "granite";
            case 4:
                return "marble"; // Quark Stone Types
            case 5:
                return "limestone";
            case 6:
                return "slate"; // Rustic Slate
            default:
                return "";
        }
    }
}
