/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public interface IProxy{

    void preInit(FMLPreInitializationEvent event);

    void addModelToRegistry(ItemStack stack, ResourceLocation location, String variant);

    String localize(String unlocalized, Object... args);

    void generateParticle(World world, BlockPos pos, int provider);

}


