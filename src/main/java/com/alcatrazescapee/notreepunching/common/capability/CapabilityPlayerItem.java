/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class CapabilityPlayerItem
{
    public static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "player_item");
    @CapabilityInject(IPlayerItem.class)
    public static final Capability<IPlayerItem> CAPABILITY = getNull();

    public static void preInit()
    {
        net.minecraftforge.common.capabilities.CapabilityManager.INSTANCE.register(IPlayerItem.class, new Storage(), Instance::new);
    }

    public static final class Instance implements IPlayerItem, ICapabilityProvider
    {
        private ItemStack stack;

        @Override
        public ItemStack getStack()
        {
            return stack == null ? ItemStack.EMPTY : stack;
        }

        @Override
        public void setStack(ItemStack stack)
        {
            this.stack = stack.copy();
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == CapabilityPlayerItem.CAPABILITY;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return hasCapability(capability, facing) ? (T) this : null;
        }
    }

    private static final class Storage implements Capability.IStorage<IPlayerItem>
    {
        @Nonnull
        @Override
        public NBTBase writeNBT(Capability<IPlayerItem> capability, IPlayerItem instance, EnumFacing facing)
        {
            NBTTagCompound nbt = new NBTTagCompound();
            return instance.getStack().writeToNBT(nbt);
        }

        @Override
        public void readNBT(Capability<IPlayerItem> capability, IPlayerItem instance, EnumFacing facing, NBTBase nbt)
        {
            if (nbt instanceof NBTTagCompound)
                instance.setStack(new ItemStack((NBTTagCompound) nbt));
        }
    }

}
