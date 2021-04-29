/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import com.alcatrazescapee.mcjunitlib.framework.IntegrationTest;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestClass;
import com.alcatrazescapee.mcjunitlib.framework.IntegrationTestHelper;
import com.alcatrazescapee.notreepunching.common.blocks.ModBlocks;
import com.alcatrazescapee.notreepunching.common.blocks.PotteryBlock;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

@IntegrationTestClass("pottery")
public class PotteryBlockTests
{
    @IntegrationTest("clay_blocks")
    public void testClayToolWorksPottery(IntegrationTestHelper helper)
    {
        int z = 0;
        for (PotteryBlock.Variant variant : PotteryBlock.Variant.values())
        {
            helper.useItem(new BlockPos(1, 0, z), Direction.UP, ModItems.CLAY_TOOL.get());
            helper.assertBlockAt(new BlockPos(1, 0, z), ModBlocks.POTTERY.get(variant).get(), "Clay tool should produce " + variant.name().toLowerCase());
            z++;
        }

        helper.useItem(new BlockPos(1, 0, 6), Direction.UP, ModItems.CLAY_TOOL.get());
        helper.assertAirAt(new BlockPos(1, 0, 6), "Clay tool on clay bucket should produce air");
    }

    @IntegrationTest("dispensers")
    public void testClayToolWorksInDispenser(IntegrationTestHelper helper)
    {
        helper.pushButton(new BlockPos(3, 1, 0));

        int z = 1;
        for (PotteryBlock.Variant variant : PotteryBlock.Variant.values())
        {
            helper.assertBlockAt(new BlockPos(1, 0, z), ModBlocks.POTTERY.get(variant).get(), "Dispenser should use clay tool on pottery and produce " + variant.name().toLowerCase());
            z++;
        }
        helper.assertAirAt(new BlockPos(1, 0, z), "Dispenser should use clay tool on clay bucket and produce air");
    }

    @IntegrationTest("pistons")
    public void testPotteryBreaksOnPistonPush(IntegrationTestHelper helper)
    {
        helper.pushButton(new BlockPos(3, 1, 0));
        int z = 1;
        for (PotteryBlock.Variant variant : PotteryBlock.Variant.values())
        {
            helper.assertAirAt(new BlockPos(1, 0, z), "Piston should break pottery " + variant.name().toLowerCase());
            z++;
        }
    }
}
