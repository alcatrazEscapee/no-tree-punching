package notreepunching.item;

import net.minecraft.item.Item;
import notreepunching.NoTreePunching;

public class ItemBase extends Item {

    public String name;

    public ItemBase(String name){
        super();

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(NoTreePunching.NTP_Tab);
    }
}
