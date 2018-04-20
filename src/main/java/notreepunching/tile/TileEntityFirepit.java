package notreepunching.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import notreepunching.block.ModBlocks;
import notreepunching.config.Config;

import javax.annotation.Nullable;

import static notreepunching.block.BlockFirepit.BURNING;

public class TileEntityFirepit extends TileEntity implements ITickable {

    private ItemStackHandler inventory = new ItemStackHandler(1);

    private int burnTicks = 30000; // Initial burn time from log that was thrown = 3000

    public void update(){
        if(!world.isRemote) {
            IBlockState state = world.getBlockState(pos);
            if(state.getValue(BURNING)){
                // Firepit is currently burning

                burnTicks--;
                if(burnTicks <= 0){
                    // Try and consume one item in fuel slot
                    ItemStack is = inventory.getStackInSlot(0);
                    if(TileEntityFurnace.getItemBurnTime(is) > 0 && TileEntityFurnace.getItemBurnTime(is) <= Config.Firepit.FUEL_MAX){
                        burnTicks += TileEntityFurnace.getItemBurnTime(is) * Config.Firepit.FUEL_MULT;
                        is.shrink(1);
                        if(is.getCount()==0){
                            is = ItemStack.EMPTY;
                        }
                        inventory.setStackInSlot(0,is);
                    } else {

                        // Else, extinguish the firepit
                        burnTicks = 0;
                        world.setBlockState(pos, ModBlocks.firepit.getDefaultState().withProperty(BURNING, false));
                    }
                }

            }
        }

    }

    // ******************** Tile Entity Methods **************** //

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)inventory : super.getCapability(capability, facing);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return (oldState.getBlock() != newState.getBlock());
    }

}
