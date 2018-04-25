package notreepunching.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.item.ModItems;
import notreepunching.client.particle.FirepitParticle;
import notreepunching.client.particle.FirepitSmokeParticle;

import java.util.Random;


@Mod.EventBusSubscriber
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModItems.registerItemModels();
        ModBlocks.registerItemBlockModels();
    }

    @Override
    public void registerItemModel(Item item, int meta, String id) {
        System.out.println("The result is: "+id);
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NoTreePunching.MODID + ":" + id, "inventory"));
    }
    @Override
    public void registerItemModel(Item item, int meta){
        registerItemModel(item, meta, item.getRegistryName().getResourcePath());
    }
    @Override
    public void registerItemModel(Item item) {
        registerItemModel(item, 0, item.getRegistryName().getResourcePath());
    }

    @Override
    public void registerItemModelWithVariant(Item item, int meta, String id, String variant){
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NoTreePunching.MODID + ":" + id,variant));
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }

    @Override
    public void generateParticle(World world, BlockPos pos, EnumParticleTypes particle){
        Random rand = new Random();
        double x = pos.getX()+0.5d+0.05d*rand.nextGaussian();
        double y = pos.getY()+0.1d;
        double z = pos.getZ()+0.5d+0.05d*rand.nextGaussian();
        if(particle == EnumParticleTypes.FLAME) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new FirepitParticle(world, x, y, z, 0d, 0.008d, 0d));
        }else if(particle == EnumParticleTypes.SMOKE_LARGE){
            Minecraft.getMinecraft().effectRenderer.addEffect(new FirepitSmokeParticle(world,x,y,z,0.01d*rand.nextGaussian(),0.03d,0.01d*rand.nextGaussian()));
        }

    }

}
