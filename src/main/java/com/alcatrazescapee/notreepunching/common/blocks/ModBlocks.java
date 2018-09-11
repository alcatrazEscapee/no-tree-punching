/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.alcatrazescapee.alcatrazcore.block.BlockCore;
import com.alcatrazescapee.alcatrazcore.util.OreDictionaryHelper;
import com.alcatrazescapee.alcatrazcore.util.RegistryHelper;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.common.tile.TileLargeVessel;
import com.alcatrazescapee.notreepunching.util.Metal;
import com.alcatrazescapee.notreepunching.util.Pottery;
import com.alcatrazescapee.notreepunching.util.Stone;

import static com.alcatrazescapee.alcatrazcore.util.CoreHelpers.getNull;
import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_BLOCKS;

@GameRegistry.ObjectHolder(value = NoTreePunching.MOD_ID)
public class ModBlocks
{
    @GameRegistry.ObjectHolder("ore/tin")
    public static final Block ORE_TIN = getNull();
    @GameRegistry.ObjectHolder("ore/copper")
    public static final Block ORE_COPPER = getNull();

    public static final Block CERAMIC_LARGE_VESSEL = getNull();

    public static void preInit()
    {
        RegistryHelper r = RegistryHelper.get(MOD_ID);
        Block block;

        // Stone Blocks
        for (Stone type : Stone.values())
        {
            if (type != Stone.STONE)
                r.registerBlock(new BlockCobble(type), "cobblestone/" + type.name(), TAB_BLOCKS);

            r.registerBlock(new BlockRock(type), null, "loose_rock/" + type.name());
            r.registerBlock(new BlockTuyere(type), null, "tuyere/" + type.name());
        }

        // Metal Blocks
        OreDictionaryHelper.register(block = new BlockCore(Material.ROCK), "ore", "copper");
        r.registerBlock(block, "ore/copper", TAB_BLOCKS);

        OreDictionaryHelper.register(block = new BlockCore(Material.ROCK), "ore", "tin");
        r.registerBlock(block, "ore/tin", TAB_BLOCKS);

        for (Metal type : Metal.values())
        {
            if (!type.isDefault)
            {
                r.registerBlock(block = new BlockCore(Material.IRON), "metal_block/" + type.name(), TAB_BLOCKS);
                OreDictionaryHelper.register(block, "block", type.name());
            }
        }

        // Pottery
        for (Pottery type : Pottery.values())
        {
            r.registerBlock(block = new BlockPottery(type), type == Pottery.WORKED ? null : ItemBlock::new, "pottery/" + type.name(), TAB_BLOCKS);
            if (type != Pottery.WORKED)
                OreDictionaryHelper.register(block, "clay", type.name());
        }

        r.registerBlock(new BlockLargeVessel(), "ceramic_large_vessel", TAB_BLOCKS);

        // Tile Entities
        r.registerTile(TileLargeVessel.class, "ceramic_large_vessel");
    }
}
