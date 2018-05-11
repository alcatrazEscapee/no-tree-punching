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
public interface IProxy{

    void preInit(FMLPreInitializationEvent event);

    void registerItemModel(Item item, int meta, String id) ;
    void registerItemModel(Item item, int meta) ;
    void registerItemModel(Item item);

    void registerItemModelWithVariant(Item item, int meta, String id, String variant);

    String localize(String unlocalized, Object... args);

    void generateParticle(World world, BlockPos pos, EnumParticleTypes particle);

}


