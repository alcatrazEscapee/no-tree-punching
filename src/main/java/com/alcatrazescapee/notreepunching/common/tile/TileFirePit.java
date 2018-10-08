/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.tile;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.tile.ITileFields;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.common.recipe.FirePitRecipe;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;

import static com.alcatrazescapee.notreepunching.common.blocks.BlockFirePit.LIT;

@ParametersAreNonnullByDefault
public class TileFirePit extends TileInventory implements ITickable, ITileFields
{
    private static final byte BURN_FIELD_ID = 0;
    private static final byte MAX_BURN_FIELD_ID = 1;
    private static final byte COOK_FIELD_ID = 2;
    public static final byte FUEL_SLOT_ID = 0;
    public static final byte INPUT_SLOT_ID = 1;
    public static final byte OUTPUT_SLOT_ID = 2;
    private static final byte NUM_FIELDS = 3;

    private static boolean isStackValidFuel(ItemStack stack)
    {
        return TileEntityFurnace.getItemBurnTime(stack) > 0 && TileEntityFurnace.getItemBurnTime(stack) <= ModConfig.BALANCE.firePitFuelMaxAmount;
    }

    private int burnTicks;
    private int maxBurnTicks;

    private int cookTimer;

    private boolean hasCachedRecipe;
    private FirePitRecipe cachedRecipe;

    public TileFirePit()
    {
        super(3);

        hasCachedRecipe = false;
        cachedRecipe = null;
    }

    public void light(boolean addStartingFuel)
    {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, true));
        if (addStartingFuel)
        {
            this.burnTicks = (int) (0.5 * TileEntityFurnace.getItemBurnTime(new ItemStack(Blocks.LOG)) * ModConfig.BALANCE.firePitFuelMultiplier);
            this.maxBurnTicks = burnTicks * 2;
        }
    }

    public void extinguish()
    {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(LIT, false));
        this.burnTicks = 0;
        this.maxBurnTicks = 0;
        this.cookTimer = 0;
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            IBlockState state = world.getBlockState(pos);
            if (world.getBlockState(pos).getValue(LIT))
            {
                if (!hasCachedRecipe)
                {
                    updateRecipe();
                }

                if (cachedRecipe != null)
                {
                    cookTimer++;
                    if (cookTimer >= ModConfig.BALANCE.firePitCookTime)
                    {
                        updateRecipe();
                        if (cachedRecipe != null)
                        {
                            ItemStack cookStack = inventory.getStackInSlot(INPUT_SLOT_ID);
                            ItemStack outStack = inventory.getStackInSlot(OUTPUT_SLOT_ID);

                            inventory.setStackInSlot(INPUT_SLOT_ID, cachedRecipe.consumeInput(cookStack));
                            inventory.setStackInSlot(OUTPUT_SLOT_ID, CoreHelpers.mergeStacks(outStack, cachedRecipe.getOutput()));

                            updateRecipe();
                        }
                        cookTimer = 0;
                    }
                }
                else if (cookTimer > 0)
                {
                    cookTimer -= 4;
                    if (cookTimer < 0)
                    {
                        cookTimer = 0;
                    }
                }

                burnTicks--;
                if (burnTicks <= 0)
                {
                    // Try and consume one item in fuel slot
                    ItemStack stack = inventory.getStackInSlot(FUEL_SLOT_ID);
                    if (isStackValidFuel(stack))
                    {
                        burnTicks += TileEntityFurnace.getItemBurnTime(stack) * ModConfig.BALANCE.firePitFuelMultiplier;
                        maxBurnTicks = burnTicks;
                        stack = CoreHelpers.consumeItem(stack);
                        inventory.setStackInSlot(FUEL_SLOT_ID, stack);
                    }
                    else
                    {
                        // Else, extinguish the fire pit
                        burnTicks = 0;
                        world.setBlockState(pos, state.withProperty(LIT, false));
                    }
                }

            }
            else
            {
                burnTicks = 0;
                maxBurnTicks = 0;
            }
        }
    }

    @Override
    public void setAndUpdateSlots(int slot)
    {
        if (!world.isRemote)
        {
            updateRecipe();
        }
        super.setAndUpdateSlots(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack)
    {
        switch (slot)
        {
            case FUEL_SLOT_ID:
                return isStackValidFuel(stack);
            case OUTPUT_SLOT_ID:
                return false;
            case INPUT_SLOT_ID:
                return true;
        }
        throw new IllegalArgumentException("Invalid slot id in isItemValid: " + slot);
    }

    @SideOnly(Side.CLIENT)
    public int getScaledCookTime()
    {
        if (ModConfig.BALANCE.firePitCookTime != 0 && cookTimer != 0)
        {
            float f1 = cookTimer / (float) ModConfig.BALANCE.firePitCookTime;
            return Math.round(23 * f1);
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public int getScaledBurnTicks()
    {
        if (maxBurnTicks != 0 && burnTicks != 0)
        {
            float f1 = burnTicks / (float) maxBurnTicks;
            return Math.round(13 * f1);
        }
        return 0;
    }

    @Override
    public int getFieldCount()
    {
        return NUM_FIELDS;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        burnTicks = nbt.getInteger("burn_ticks");
        maxBurnTicks = nbt.getInteger("max_burn_ticks");
        cookTimer = nbt.getInteger("cook_ticks");
        super.readFromNBT(nbt);
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("burn_ticks", burnTicks);
        nbt.setInteger("max_burn_ticks", maxBurnTicks);
        nbt.setInteger("cook_ticks", cookTimer);
        return super.writeToNBT(nbt);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    private void updateRecipe()
    {
        hasCachedRecipe = true;
        ItemStack cookStack = inventory.getStackInSlot(INPUT_SLOT_ID);
        FirePitRecipe recipe = ModRecipes.FIRE_PIT.get(cookStack);

        if (recipe != null)
        {
            ItemStack outStack = inventory.getStackInSlot(OUTPUT_SLOT_ID);
            ItemStack output = recipe.getOutput();
            // check if the stacks can be merged in the output
            if (CoreHelpers.canMergeStacks(outStack, output))
            {
                cachedRecipe = recipe;
                return;
            }
        }
        cachedRecipe = null;
    }

    @Override
    public int getField(int id)
    {
        switch (id)
        {
            case BURN_FIELD_ID:
                return burnTicks;
            case MAX_BURN_FIELD_ID:
                return maxBurnTicks;
            case COOK_FIELD_ID:
                return cookTimer;
            default:
                NoTreePunching.getLog().warn("Invalid field ID: {}", id);
                return 0;
        }
    }

    @Override
    public void setField(int id, int value)
    {
        switch (id)
        {
            case BURN_FIELD_ID:
                burnTicks = value;
                break;
            case MAX_BURN_FIELD_ID:
                maxBurnTicks = value;
                break;
            case COOK_FIELD_ID:
                cookTimer = value;
                break;
            default:
                NoTreePunching.getLog().warn("Invalid Field ID {}", id);
        }
    }
}
