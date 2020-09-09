/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.compat;


import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;

import com.alcatrazescapee.notreepunching.common.items.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@JeiPlugin
public final class NoTreePunchingJEIPlugin implements IModPlugin
{
    @Override
    public ResourceLocation getPluginUid()
    {
        return new ResourceLocation(MOD_ID, "plugin");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration)
    {
        registration.registerSubtypeInterpreter(ModItems.CERAMIC_BUCKET.get(), itemStack ->
            itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
                .filter(handler -> handler instanceof FluidHandlerItemStackSimple)
                .map(handler -> ((FluidHandlerItemStackSimple) handler).getFluid().getDisplayName().getString())
                .orElse("")
        );
    }
}