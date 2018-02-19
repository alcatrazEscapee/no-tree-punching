package notreepunching.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemTool;
import notreepunching.NoTreePunching;

import java.util.Set;

public class ItemCrudePick extends ItemPickaxe {

    public String name;
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.IRON_ORE,Blocks.STONE,Blocks.COBBLESTONE,Blocks.GRAVEL);

    public ItemCrudePick(ToolMaterial material, String name){

        super(material);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(NoTreePunching.NTP_Tab);
    }

    public boolean shouldBreakBlock(Block block){
        return EFFECTIVE_ON.contains(block);
    }
}
