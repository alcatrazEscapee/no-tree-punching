package notreepunching.proxy;


import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


@Mod.EventBusSubscriber
public class CommonProxy{

    public void preInit(FMLPreInitializationEvent event){
    }

    public void init(FMLInitializationEvent event){
    }

    public void postInit(FMLPostInitializationEvent event){
    }

    public void registerItemModel(Item item, int meta, String id) {
    }
    public void registerItemModel(Item item, int meta) {
    }
    public void registerItemModel(Item item){
    }

    public void registerItemModelWithVariant(Item item, int meta, String id, String variant){
    }

    public String localize(String unlocalized, Object... args) {
        return unlocalized;
    }

    public void generateParticle(World world, BlockPos pos, EnumParticleTypes particle){
    }

}


