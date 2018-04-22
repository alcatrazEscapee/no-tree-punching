package notreepunching.item;

import net.minecraft.item.ItemSpade;
import notreepunching.NoTreePunching;

public class ItemCrudeShovel extends ItemSpade {

    public String name;

    public ItemCrudeShovel(ToolMaterial material, String name){
        super(material);

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(NoTreePunching.NTP_Tab);
    }
}
