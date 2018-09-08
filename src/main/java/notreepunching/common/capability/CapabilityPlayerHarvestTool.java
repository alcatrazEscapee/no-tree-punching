/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package notreepunching.common.capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import static notreepunching.NoTreePunching.MOD_ID;

public class CapabilityPlayerHarvestTool
{
    public static final ResourceLocation KEY = new ResourceLocation(MOD_ID, "harvest_tool");
    @CapabilityInject(IPlayerHarvestTool.class)
    public static Capability<IPlayerHarvestTool> HARVEST_TOOL_CAPABILITY = null;

    public static void preInit()
    {
        CapabilityManager.INSTANCE.register(IPlayerHarvestTool.class, new Storage(), Default::new);
    }

    public static class Default implements IPlayerHarvestTool, ICapabilityProvider
    {
        private ItemStack stack;

        @Override
        public ItemStack getStack()
        {
            return stack;
        }

        @Override
        public void setStack(ItemStack stack)
        {
            this.stack = stack.copy();
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
        {
            return capability == CapabilityPlayerHarvestTool.HARVEST_TOOL_CAPABILITY;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
        {
            return hasCapability(capability, facing) ? (T) this : null;
        }
    }

    private static class Storage implements Capability.IStorage<IPlayerHarvestTool>
    {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IPlayerHarvestTool> capability, IPlayerHarvestTool instance, EnumFacing facing)
        {
            NBTTagCompound nbt = new NBTTagCompound();
            return instance.getStack().writeToNBT(nbt);
        }

        @Override
        public void readNBT(Capability<IPlayerHarvestTool> capability, IPlayerHarvestTool instance, EnumFacing facing, NBTBase nbt)
        {
            if (nbt instanceof NBTTagCompound)
                instance.setStack(new ItemStack((NBTTagCompound) nbt));
        }
    }

}
