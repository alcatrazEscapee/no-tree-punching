package notreepunching.item;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import notreepunching.NoTreePunching;

import java.util.Set;

public class ItemCrudeAxe extends ItemAxe {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.LOG, Blocks.LOG2);
    public String name;

    public ItemCrudeAxe(ToolMaterial material, String name){
        super(material,4.0F,-3.0F);

        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(NoTreePunching.NTP_Tab);
    }

    public boolean shouldBreakBlock(Block block){
        return EFFECTIVE_ON.contains(block);
    }
}
