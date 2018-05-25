package notreepunching.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class BlockTuyere extends BlockBase {

    public BlockTuyere(String name){
        super(name, Material.ROCK);
    }

    @Override
    public void register() {
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name, false);
    }
}
