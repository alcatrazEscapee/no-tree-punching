/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.common.tileentity.InventoryTileEntity;
import com.alcatrazescapee.notreepunching.common.tileentity.LargeVesselTileEntity;
import com.alcatrazescapee.notreepunching.util.Helpers;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static final RegistryObject<MenuType<LargeVesselContainer>> LARGE_VESSEL = register("large_vessel", LargeVesselTileEntity.class, LargeVesselContainer::new);
    public static final RegistryObject<MenuType<SmallVesselContainer>> SMALL_VESSEL = register("small_vessel", (windowId, playerInv, buffer) -> new SmallVesselContainer(windowId, playerInv));

    @SuppressWarnings("SameParameterValue")
    private static <T extends InventoryTileEntity, C extends DeviceContainer<T>> RegistryObject<MenuType<C>> register(String name, Class<T> tileClass, DeviceContainer.IFactory<T, C> factory)
    {
        return register(name, (windowId, playerInventory, packetBuffer) -> {
            Level world = playerInventory.player.level;
            BlockPos pos = packetBuffer.readBlockPos();
            return Helpers.getTE(world, pos, tileClass).map(tile -> factory.create(tile, playerInventory, windowId)).orElse(null);
        });
    }

    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(String name, IContainerFactory<C> containerFactory)
    {
        return CONTAINERS.register(name, () -> IForgeContainerType.create(containerFactory));
    }
}