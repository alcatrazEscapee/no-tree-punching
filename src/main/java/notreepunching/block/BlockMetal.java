package notreepunching.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockMetal extends BlockBase {

    BlockMetal(String name){
        super(name, Material.ROCK);

        if(name.contains("block")) {
            setHardness(4.0F);
            setResistance(25.0F);
            setHarvestLevel("pickaxe", 1);
        }else if(name.contains("ore")){
            setHardness(2.5F);
            setHarvestLevel("pickaxe", 0);
        }
        setSoundType(SoundType.STONE);
    }
}
