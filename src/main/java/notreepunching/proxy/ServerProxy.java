package notreepunching.proxy;

import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;


import java.util.Random;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy implements IProxy {

    public void preInit(FMLPreInitializationEvent event){
    }

    public String localize(String unlocalized, Object... args) {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }

    public void registerItemModel(Item item, int meta, String id) {
    }
    public void registerItemModel(Item item, int meta){
    }
    public void registerItemModel(Item item) {
    }

    public void registerItemModelWithVariant(Item item, int meta, String id, String variant){
    }

    public void generateParticle(World world, BlockPos pos, EnumParticleTypes particle){

    }
}
