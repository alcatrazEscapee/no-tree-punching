package notreepunching.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.NoTreePunching;
import notreepunching.block.ModBlocks;
import notreepunching.item.ModItems;
import notreepunching.particle.FirePitParticle;

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
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NoTreePunching.MODID + ":" + id, "inventory"));
    }

    @Override
    public void registerItemModelWithVariant(Item item, int meta, String id, String variant){
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NoTreePunching.MODID + ":" + id,variant));
    }

    @Override
    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }

    public void generateTEParticle(TileEntity te, EnumParticleTypes particle){
        Random rand = new Random();
        double x = te.getPos().getX()+0.5d+0.05d*rand.nextGaussian();
        double y = te.getPos().getY()+0.1d;
        double z = te.getPos().getZ()+0.5d+0.05d*rand.nextGaussian();
        Minecraft.getMinecraft().effectRenderer.addEffect(new FirePitParticle(te.getWorld(),x,y,z,0d,0.01d,0d));
        //SBParticle particle2 = new SBParticle(te.getWorld(),x,y,z,targetX, targetY, targetZ,10, col.toInt(),1F);


        //Minecraft.getMinecraft().effectRenderer.addEffect(particle2);
    }

}
