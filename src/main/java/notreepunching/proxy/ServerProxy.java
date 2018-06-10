package notreepunching.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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

    public void addModelToRegistry(ItemStack stack, ResourceLocation location, String variant){
    }

    @SuppressWarnings("deprecation")
    public String localize(String unlocalized, Object... args) {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }

    public void generateParticle(World world, BlockPos pos, int provider){

    }
}
