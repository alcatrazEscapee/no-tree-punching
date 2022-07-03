package com.alcatrazescapee.notreepunching.platform.event;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ContainerFactory<T extends AbstractContainerMenu>
{
    @Nullable
    T create(int windowId, Inventory inventory, FriendlyByteBuf buffer);
}
