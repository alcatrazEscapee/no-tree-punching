/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.alcatrazescapee.alcatrazcore.block.BlockTileCore;
import com.alcatrazescapee.alcatrazcore.util.CoreHelpers;
import com.alcatrazescapee.alcatrazcore.util.compat.FireRegistry;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.client.ModGuiHandler;
import com.alcatrazescapee.notreepunching.client.particle.ParticleManager;
import com.alcatrazescapee.notreepunching.common.tile.TileFirePit;

@ParametersAreNonnullByDefault
public class BlockFirePit extends BlockTileCore
{
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockFirePit()
    {
        super(Material.WOOD);

        setTickRandomly(true);
        setDefaultState(this.blockState.getBaseState().withProperty(LIT, true));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel()
    {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LIT).build());
        super.registerModel();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileFirePit();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(LIT, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LIT) ? 1 : 0;
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.375D, 0D, 0.375D, 0.615D, 0.0625D, 0.625D);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @Nullable
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    @Deprecated
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
        if (!worldIn.isRemote)
        {
            if (worldIn.canBlockSeeSky(pos) && worldIn.isRaining() && worldIn.getTopSolidOrLiquidBlock(pos).getY() < pos.getY() + 2)
            {
                worldIn.setBlockState(pos, state.withProperty(LIT, false));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (stateIn.getValue(LIT))
        {
            ParticleManager.generateFirePitParticle(worldIn, pos);
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            if (worldIn.canBlockSeeSky(pos) && worldIn.isRaining() && worldIn.getTopSolidOrLiquidBlock(pos).getY() < pos.getY() + 2)
            {
                if (rand.nextDouble() < 0.4D)
                {
                    worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
                    ParticleManager.generateFirePitParticle(worldIn, pos);
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            ItemStack stack = player.getHeldItem(hand);
            // Special Interactions
            if (state.getValue(LIT))
            {
                if (CoreHelpers.doesStackMatchOre(stack, "sand"))
                {
                    // Extinguish
                    world.setBlockState(pos, this.getDefaultState().withProperty(LIT, false));
                    player.setHeldItem(hand, CoreHelpers.consumeItem(player, stack, 1));
                    return true;
                }
                if (CoreHelpers.doesStackMatchOre(stack, "stickWood"))
                {
                    // Burn the end of a stick into a torch
                    ItemStack torchStack = new ItemStack(Blocks.TORCH, 2);
                    if (!player.addItemStackToInventory(torchStack))
                    {
                        CoreHelpers.dropItemInWorldExact(world, player.getPosition(), torchStack);
                    }
                    player.setHeldItem(hand, CoreHelpers.consumeItem(player, stack, 1));
                    world.playSound(null, player.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 0.5F, 1.5F);
                    return true;
                }
            }
            else
            {
                if (FireRegistry.isFireStarter(stack))
                {
                    // Light an fire pit
                    world.setBlockState(pos, this.getDefaultState().withProperty(LIT, true));
                    stack.damageItem(1, player);
                    return true;
                }
            }
            if (!player.isSneaking())
            {
                player.openGui(NoTreePunching.getInstance(), ModGuiHandler.FIRE_PIT, world, pos.getX(), pos.getY(), pos.getZ());
            }
        }
        return true;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LIT);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        drops.clear();
        drops.add(new ItemStack(Items.STICK, 2, 0));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        if (!worldIn.isRemote)
        {
            // Breaks block if the block under it breaks.
            IBlockState stateUnder = worldIn.getBlockState(pos.down());
            if (!stateUnder.isNormalCube())
            {
                dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }
}
