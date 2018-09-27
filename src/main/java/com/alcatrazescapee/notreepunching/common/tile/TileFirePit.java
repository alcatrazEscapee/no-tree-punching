/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.tile;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.recipe.RecipeCore;
import com.alcatrazescapee.alcatrazcore.tile.ITileFields;
import com.alcatrazescapee.alcatrazcore.tile.TileInventory;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.notreepunching.ModConfig;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.common.recipe.ModRecipes;

import static com.alcatrazescapee.notreepunching.common.blocks.BlockFirePit.LIT;

@ParametersAreNonnullByDefault
public class TileFirePit extends TileInventory implements ITickable, ITileFields
{
    private static final byte BURN_FIELD_ID = 0;
    private static final byte MAX_BURN_FIELD_ID = 1;
    private static final byte COOK_FIELD_ID = 2;
    private static final byte MAX_COOK_FIELD_ID = 3;

    public static boolean isStackValidFuel(ItemStack stack)
    {
        return TileEntityFurnace.getItemBurnTime(stack) > 0 && TileEntityFurnace.getItemBurnTime(stack) <= ModConfig.GENERAL.firePitFuelMaxAmount;
    }

    private int burnTicks;
    private int maxBurnTicks;

    private int cookTimer = 0;
    private int maxCookTimer = 0;

    public TileFirePit()
    {
        super(3);
    }

    @Override
    public void update()
    {
        if (!world.isRemote)
        {
            IBlockState state = world.getBlockState(pos);
            if (world.getBlockState(pos).getValue(LIT))
            {
                // Try and cook the item in the fire pit
                ItemStack cookStack = inventory.getStackInSlot(1);
                ItemStack outStack = inventory.getStackInSlot(2);

                RecipeCore recipe = ModRecipes.FIRE_PIT.get(cookStack);
                if (recipe != null)
                {
                    ItemStack output = recipe.getOutput();
                    // check if the stacks can be merged in the output
                    if (CoreHelpers.canMergeStacks(outStack, output))
                    {
                        cookTimer++;
                        maxCookTimer = ModConfig.GENERAL.firePitCookSpeed;

                        if (cookTimer >= maxCookTimer)
                        {
                            inventory.setStackInSlot(1, recipe.consumeInput(cookStack));
                            inventory.setStackInSlot(2, CoreHelpers.mergeStacks(outStack, output));
                            cookTimer = 0;
                        }
                    }
                }
                else
                {
                    cookTimer -= 2;
                    // Don't reset the max coo
                    //maxCookTimer = 0;
                }

                burnTicks--;
                if (burnTicks <= 0)
                {
                    // Try and consume one item in fuel slot
                    ItemStack is = inventory.getStackInSlot(0);
                    if (isStackValidFuel(is))
                    {
                        burnTicks += TileEntityFurnace.getItemBurnTime(is) * ModConfig.GENERAL.firePitFuelMultiplier;
                        maxBurnTicks = burnTicks;
                        is = CoreHelpers.consumeItem(is);
                        inventory.setStackInSlot(0, is);
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

    @SideOnly(Side.CLIENT)
    public int getScaledCookTime()
    {
        if (maxCookTimer != 0 && cookTimer != 0)
        {
            float f1 = cookTimer / (float) maxCookTimer;
            return Math.round(23 * f1);
        }
        return 0;
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
        return (oldState.getBlock() != newState.getBlock());
    }

    @Override
    public int getFieldCount()
    {
        return 4;
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
            case MAX_COOK_FIELD_ID:
                return maxCookTimer;
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
            case MAX_COOK_FIELD_ID:
                maxCookTimer = value;
                break;
            default:
                NoTreePunching.getLog().warn("Invalid Field ID {}", id);
        }
    }
}
