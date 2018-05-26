package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;

public class BlockBase extends Block implements IHasItemBlockModel {

    public String name;

    public BlockBase(String name, Material material){
        super(material);

        this.name = name;
        this.register();
    }

    public void register(){
        ModBlocks.addBlockToRegistry(this, new ItemBlock(this), name, ModTabs.ITEMS_TAB);
    }
    public void addModelToRegistry(){
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    @Override
    public BlockBase setHardness(float hardness){
        super.setHardness(hardness);
        return this;
    }
    @Override
    public BlockBase setResistance(float resistance){
        super.setResistance(resistance);
        return this;
    }
    @Override
    public BlockBase setSoundType(SoundType soundType){
        super.setSoundType(soundType);
        return this;
    }
}
