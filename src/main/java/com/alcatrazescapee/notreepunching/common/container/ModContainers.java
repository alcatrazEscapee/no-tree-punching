package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;

import com.alcatrazescapee.notreepunching.common.blockentity.ModBlockEntities;
import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public class ModContainers
{
    public static final RegistryInterface<MenuType<?>> CONTAINERS = XPlatform.INSTANCE.registryInterface(Registry.MENU);

    public static final RegistryHolder<MenuType<LargeVesselContainer>> LARGE_VESSEL = register("large_vessel", (windowId, playerInventory, packetBuffer) -> {
        final Level level = playerInventory.player.level;
        final BlockPos pos = packetBuffer.readBlockPos();
        return level.getBlockEntity(pos, ModBlockEntities.LARGE_VESSEL.get())
            .map(vessel -> vessel.createMenu(windowId, playerInventory, playerInventory.player))
            .orElse(null);
    });
    public static final RegistryHolder<MenuType<SmallVesselContainer>> SMALL_VESSEL = register("small_vessel", (windowId, playerInv, buffer) -> new SmallVesselContainer(windowId, playerInv, buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));

    private static <C extends AbstractContainerMenu> RegistryHolder<MenuType<C>> register(String name, IContainerFactory<C> containerFactory)
    {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(containerFactory));
    }
}