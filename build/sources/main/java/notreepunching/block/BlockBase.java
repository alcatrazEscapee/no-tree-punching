package notreepunching.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import notreepunching.NoTreePunching;

public class BlockBase extends Block {

    public String name;

    public BlockBase(String name, Material material){
        super(material);

        setUnlocalizedName(name);
        setRegistryName(name);
        this.name = name;
        //setCreativeTab(NoTreePunching.NTP_Tab);
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
