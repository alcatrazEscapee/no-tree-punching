/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.tileentity;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class ModTileEntities
{
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static final RegistryObject<TileEntityType<LargeVesselTileEntity>> LARGE_VESSEL = register("large_vessel", LargeVesselTileEntity::new, ModBlocks.CERAMIC_LARGE_VESSEL);

    @SuppressWarnings({"ConstantConditions", "SameParameterValue"})
    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> tileEntityFactory, Supplier<? extends Block> blockFactory)
    {
        return TILE_ENTITIES.register(name, () -> TileEntityType.Builder.create(tileEntityFactory, blockFactory.get()).build(null));
    }
}
