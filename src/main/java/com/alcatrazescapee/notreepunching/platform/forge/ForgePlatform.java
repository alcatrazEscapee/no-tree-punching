package com.alcatrazescapee.notreepunching.platform.forge;

import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public final class ForgePlatform implements XPlatform
{
    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new ForgeRegistryInterface<>(registry);
    }

    @Override
    public CreativeModeTab creativeTab(ResourceLocation id, Supplier<ItemStack> icon)
    {
        return new CreativeModeTab(id.toString()) {
            @Override
            public ItemStack makeIcon()
            {
                return icon.get();
            }
        };
    }

    @Override
    public boolean isDedicatedClient()
    {
        return FMLLoader.getDist() == Dist.CLIENT;
    }
}
