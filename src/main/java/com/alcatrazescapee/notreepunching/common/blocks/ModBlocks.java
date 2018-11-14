/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.notreepunching.common.tile.TileFirePit;
import com.alcatrazescapee.notreepunching.common.tile.TileLargeVessel;
import com.alcatrazescapee.notreepunching.util.types.Pottery;
import com.alcatrazescapee.notreepunching.util.types.Stone;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_ITEMS;

@GameRegistry.ObjectHolder(value = MOD_ID)
public final class ModBlocks
{
    public static final Block CERAMIC_LARGE_VESSEL = getNull();
    public static final Block FIRE_PIT = getNull();

    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);
        Block block;

        // Stone Blocks
        for (Stone type : Stone.values())
        {
            if (type.isEnabled())
            {
                if (type.hasCobblestone())
                    r.registerBlock(new BlockCobble(type), "cobblestone/" + type.name(), TAB_ITEMS);

                r.registerBlock(new BlockRock(type), null, "loose_rock/" + type.name());
            }
        }

        // Pottery
        for (Pottery type : Pottery.values())
        {
            r.registerBlock(block = new BlockPottery(type), type == Pottery.WORKED ? null : ItemBlock::new, "pottery/" + type.name(), TAB_ITEMS);
            if (type != Pottery.WORKED)
                OreDictionaryHelper.register(block, "clay", type.name());
        }

        r.registerBlock(new BlockLargeVessel(), "ceramic_large_vessel", TAB_ITEMS);
        r.registerBlock(new BlockFirePit(), null, "fire_pit");

        // Tile Entities
        r.registerTile(TileLargeVessel.class, "ceramic_large_vessel");
        r.registerTile(TileFirePit.class, "fire_pit");
    }
}
