package com.alcatrazescapee.notreepunching.platform;

import java.util.function.Supplier;

public interface RegistryInterface<T>
{
    default void earlySetup() {}

    default void lateSetup() {}

    <V extends T> RegistryHolder<V> register(String name, Supplier<? extends V> factory);
}
