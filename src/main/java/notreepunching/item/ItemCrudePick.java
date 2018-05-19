package notreepunching.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;

import java.util.Set;

public class ItemCrudePick extends ItemPickaxe {

    public String name;
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.IRON_ORE,Blocks.COAL_ORE,Blocks.STONE,Blocks.COBBLESTONE,Blocks.GRAVEL);

    public ItemCrudePick(ToolMaterial material, String name){

        super(material);
        this.name = name;
        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name,true);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    public boolean shouldBreakBlock(Block block){
        return EFFECTIVE_ON.contains(block);
    }
}
