package com.alcatrazescapee.notreepunching.platform;

import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface RegistryHolder<T> extends Supplier<T>
{
    @Override
    T get();

    ResourceLocation id();

    Registry<T> registry();

    default ResourceKey<T> key()
    {
        return ResourceKey.create(registry().key(), id());
    }

    default Holder<T> holder()
    {
        return registry().getHolderOrThrow(key());
    }

}
