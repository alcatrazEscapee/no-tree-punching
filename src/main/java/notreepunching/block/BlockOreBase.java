package notreepunching.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockOreBase extends BlockBase {

    public BlockOreBase(String name){
        super(name, Material.ROCK);

        setHardness(2.5F);
        setHarvestLevel("pickaxe",0);
        setSoundType(SoundType.STONE);
    }
}
