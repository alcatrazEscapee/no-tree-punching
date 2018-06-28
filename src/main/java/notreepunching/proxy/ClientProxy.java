/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import notreepunching.client.particle.ParticleGenerator;
import notreepunching.registry.ClientRegistryHandler;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy implements IProxy {

    public void preInit(FMLPreInitializationEvent event){
        MinecraftForge.EVENT_BUS.register(new ClientRegistryHandler());
    }

    public void addModelToRegistry(ItemStack stack, ResourceLocation location, String variant){
        ClientRegistryHandler.MODEL_REGISTRY.put(stack, new ModelResourceLocation(location, variant));
    }

    public String localize(String unlocalized, Object... args) {
        return I18n.format(unlocalized, args);
    }

    public void generateParticle(World world, BlockPos pos, int provider){
        switch(provider){
            case 0:
                ParticleGenerator.firepitParticle(world, pos);
                break;
            case 1:
                ParticleGenerator.forgeParticle(world, pos);
                break;
            case 2:
                ParticleGenerator.firestarterParticle(world, pos);
                break;
            case 3:
                ParticleGenerator.woodPileParticle(world, pos);
                break;
        }

    }

}
