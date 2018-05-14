package notreepunching.block;

import net.minecraft.block.material.Material;

public class BlockWoodPile extends BlockBase {

    public BlockWoodPile(String name){
        super(name, Material.WOOD);

        setHardness(1.2F);
    }
}
