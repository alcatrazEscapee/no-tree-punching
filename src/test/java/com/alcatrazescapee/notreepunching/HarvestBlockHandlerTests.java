/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import com.alcatrazescapee.notreepunching.common.items.ModItems;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HarvestBlockHandlerTests
{
    @Test
    public void testDirtBreaksWithEmptyHand()
    {
        doTest(Blocks.DIRT, Items.AIR, true);
    }

    @Test
    public void testStoneUnbreakableWithEmptyHand()
    {
        doTest(Blocks.STONE, Items.AIR, false);
    }

    @Test
    public void testLogUnbreakableWithEmptyHand()
    {
        doTest(Blocks.ACACIA_LOG, Items.AIR, false);
    }

    @Test
    public void testLeavesBreaksWithEmptyHand()
    {
        doTest(Blocks.ACACIA_LEAVES, Items.AIR, true);
    }

    @Test
    public void testGravelBreaksWithEmptyHand()
    {
        doTest(Blocks.GRAVEL, Items.AIR, true);
    }

    @Test
    public void testLogBreaksWithAxes()
    {
        doTest(Blocks.BIRCH_LOG, Items.IRON_AXE, true);
        doTest(Blocks.BIRCH_LOG, ModItems.FLINT_AXE.get(), true);
    }

    @Test
    public void testLogUnbreakableWithPickaxe()
    {
        doTest(Blocks.DARK_OAK_LOG, Items.DIAMOND_PICKAXE, false);
    }

    @Test
    public void testConcretePowderUnbreakableWithEmptyHand()
    {
        doTest(Blocks.BLACK_CONCRETE_POWDER, Items.AIR, false);
    }

    @Test
    public void testConcretePowderBreaksWithShovelOrPickaxe()
    {
        doTest(Blocks.BLACK_CONCRETE_POWDER, Items.IRON_SHOVEL, true);
    }

    @Test
    public void testConcretePowderBreaksWithPickaxe()
    {
        doTest(Blocks.BLACK_CONCRETE_POWDER, Items.IRON_PICKAXE, true);
    }

    @Test
    public void testPoppyWithEmptyHand()
    {
        Config.SERVER.doInstantBreakBlocksDropWithoutCorrectTool.set(true);
        doTest(Blocks.POPPY, Items.AIR, true, "Should drop if no tool required");

        Config.SERVER.doInstantBreakBlocksDropWithoutCorrectTool.set(false);
        doTest(Blocks.POPPY, Items.AIR, false, "Shouldn't drop when a tool is required");

        // Reset defaults
        Config.SERVER.doInstantBreakBlocksDropWithoutCorrectTool.set(true);
    }

    @Test
    public void testFlowerBreaksWithAxe()
    {
        doTest(Blocks.POPPY, Items.IRON_AXE, true);
    }

    @Test
    public void testFlowerBreaksWithShears()
    {
        doTest(Blocks.POPPY, Items.SHEARS, true);
    }

    @Test
    public void testVinesUnbreakableWithEmptyHand()
    {
        doTest(Blocks.VINE, Items.AIR, false);
    }

    @Test
    public void testVinesBreakWithAxe()
    {
        doTest(Blocks.VINE, Items.IRON_AXE, true);
    }

    @Test
    public void testVinesBreakWithShears()
    {
        doTest(Blocks.VINE, Items.SHEARS, true);
    }

    @Test
    public void testIronOreBreakableWithFlintPickaxe()
    {
        doTest(Blocks.IRON_ORE, ModItems.FLINT_PICKAXE.get(), true);
    }

    @Test
    public void testHayBaleUnbreakableWithEmptyHand()
    {
        doTest(Blocks.HAY_BLOCK, Items.AIR, false);
    }

    @Test
    public void testHayBaleBreaksWithHoe()
    {
        doTest(Blocks.HAY_BLOCK, Items.IRON_HOE, true);
    }

    private void doTest(Block block, Item item, boolean shouldHarvest)
    {
        doTest(block, item, shouldHarvest, "Block " + block.getRegistryName() + " with item " + item.getRegistryName() + " should harvest = " + shouldHarvest);
    }

    private void doTest(Block block, Item item, boolean shouldHarvest, String message)
    {
        final PlayerEntity player = FakePlayerFactory.getMinecraft(ServerLifecycleHooks.getCurrentServer().overworld());
        final ItemStack stack = new ItemStack(item);
        final BlockState state = block.defaultBlockState();

        player.setItemInHand(Hand.MAIN_HAND, stack);
        assertEquals(shouldHarvest, player.hasCorrectToolForDrops(state), message);
    }
}
