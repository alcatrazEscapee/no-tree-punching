/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.container;

import java.util.function.Supplier;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.alcatrazescapee.notreepunching.common.blockentity.InventoryBlockEntity;
import com.alcatrazescapee.notreepunching.common.blockentity.LargeVesselBlockEntity;
import com.alcatrazescapee.notreepunching.common.blockentity.ModBlockEntities;
import com.alcatrazescapee.notreepunching.util.Helpers;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModContainers
{
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static final RegistryObject<MenuType<LargeVesselContainer>> LARGE_VESSEL = register("large_vessel", ModBlockEntities.LARGE_VESSEL, LargeVesselContainer::new);
    public static final RegistryObject<MenuType<SmallVesselContainer>> SMALL_VESSEL = register("small_vessel", (windowId, playerInv, buffer) -> new SmallVesselContainer(windowId, playerInv, buffer.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));

    @SuppressWarnings("SameParameterValue")
    private static <T extends InventoryBlockEntity, C extends DeviceContainer<T>> RegistryObject<MenuType<C>> register(String name, Supplier<BlockEntityType<T>> entity, DeviceContainer.IFactory<T, C> factory)
    {
        return register(name, (windowId, playerInventory, packetBuffer) -> {
            final Level level = playerInventory.player.level;
            final BlockPos pos = packetBuffer.readBlockPos();
            return level.getBlockEntity(pos, entity.get()).map(tile -> factory.create(tile, playerInventory, windowId)).orElse(null);
        });
    }

    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> register(String name, IContainerFactory<C> containerFactory)
    {
        return CONTAINERS.register(name, () -> IForgeMenuType.create(containerFactory));
    }
}