/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.block.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import notreepunching.block.ModBlocks;
import notreepunching.config.ModConfig;
import notreepunching.util.MiscUtil;

import javax.annotation.ParametersAreNonnullByDefault;

import static notreepunching.block.BlockCharcoalPile.LAYERS;
import static notreepunching.block.BlockWoodPile.ONFIRE;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TileEntityWoodPile extends TileEntitySidedInventory implements ITickable {

    public boolean burning;
    private int burnTicks;
    private final int maxBurnTicks;

    private static final int NUM_SLOTS = 4;

    public TileEntityWoodPile(){
        super(NUM_SLOTS);

        burnTicks = 0;
        maxBurnTicks = ModConfig.Balance.CHARCOAL_PIT_TIMER;
        burning = false;
    }

    public void update(){
        if(world.isRemote) { return; }

        if (burning) {
            if(burnTicks < maxBurnTicks){
                burnTicks ++;
            }else{
                // Attempt to turn this log pile into charcoal
                createCharcoal();
            }
        }else{
            if(world.getBlockState(pos.up()).getBlock() == Blocks.FIRE){
                burning = true;
            }
        }
    }

    @Override
    public void setAndUpdateSlots(int slot){
        if(world.isRemote){ return; }
        for(int i = 0; i < 4; i++){
            if(!inventory.getStackInSlot(i).isEmpty()){
                return;
            }
        }

        world.setBlockToAir(pos);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 4;
    }

    public static boolean isItemValid(ItemStack stack) {
        if(!stack.isEmpty()){
            return MiscUtil.doesStackMatchOre(stack, "logWood");
        }
        return true;
    }

    public void insertLog(ItemStack stack){
        inventory.setStackInSlot(0, stack);
    }

    // This function does some magic fuckery to not create floating charcoal. Don't touch unless broken
    private void createCharcoal(){
        int j = 0;
        Block block;
        do {
            j++;
            block = world.getBlockState(pos.down(j)).getBlock();
            // This is here so that the charcoal pile will collapse Bottom > Top
            // Because the pile scans Top > Bottom this is nessecary to avoid floating blocks
            if(block == ModBlocks.woodPile){ return; }
        } while (block == Blocks.AIR || block == ModBlocks.charcoalPile);

        double logs = (double) countLogs();
        double log2 = 0.008d*logs*(logs + 42.5d) - 0.75d + 1.5d*Math.random();
        int charcoal = (int) Math.min(8,Math.max(0,Math.round(log2)));
        if(charcoal == 0){
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            return;
        }
        if(j == 1){
            // This log pile is at the bottom of the charcoal pit
            world.setBlockState(pos,ModBlocks.charcoalPile.getDefaultState().withProperty(LAYERS, charcoal));
            return;
        }
        for(int k = j - 1; k >= 0; k--){
            // Climb back up from the bottom
            IBlockState state = world.getBlockState(pos.down(k));
            if(state.getBlock() == Blocks.AIR){
                // If it hits air, place the remaining pile in that block
                world.setBlockState(pos.down(k), ModBlocks.charcoalPile.getDefaultState().withProperty(LAYERS, charcoal));
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return;
            }

            if(state.getBlock() == ModBlocks.charcoalPile){
                // Place what it can in the existing charcoal pit, then continue climbing
                charcoal += state.getValue(LAYERS);
                int toCreate = charcoal > 8 ? 8 : charcoal;
                world.setBlockState(pos.down(k), ModBlocks.charcoalPile.getDefaultState().withProperty(LAYERS, toCreate));
                charcoal -= toCreate;
            }

            if(charcoal <= 0){
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
                return;
            }
        }
        // If you exit the loop, its arrived back at the original position OR needs to rest the original position, and needs to replace that block
        world.setBlockState(pos, ModBlocks.charcoalPile.getDefaultState().withProperty(LAYERS, charcoal));
    }

    public void tryLightNearby(World world, BlockPos pos){
        for(EnumFacing side : EnumFacing.values()){
            IBlockState state = world.getBlockState(pos.offset(side));
            if(state.getBlock() == ModBlocks.woodPile){
                if(state.getValue(ONFIRE)) continue;
                world.setBlockState(pos.offset(side),state.withProperty(ONFIRE, true));
                TileEntityWoodPile tile = (TileEntityWoodPile) world.getTileEntity(pos.offset(side));
                if(tile == null) continue;
                tile.burning = true;
                tile.tryLightNearby(world, pos.offset(side));
            }
        }
    }

    public int countLogs(){
        int logs = 0;
        for(int i = 0; i < inventory.getSlots(); i++){
            logs += inventory.getStackInSlot(i).getCount();
        }
        return logs;
    }

    @Override
    protected NBTTagCompound writeNBT(NBTTagCompound c) {
        c.setInteger("burn_ticks",burnTicks);
        c.setBoolean("burning", burning);
        return c;
    }

    protected void readNBT(NBTTagCompound c){
        burnTicks = c.getInteger("burn_ticks");
        burning = c.getBoolean("burning");
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return (oldState.getBlock() != newState.getBlock());
    }

}
