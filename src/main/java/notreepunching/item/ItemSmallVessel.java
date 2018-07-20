/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.inventory.ItemStackInventoryWrapper;
import notreepunching.client.ModGuiHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemSmallVessel extends ItemBase {

    ItemSmallVessel(String name){
        super(name);

        setMaxStackSize(1);
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);
        if(!world.isRemote){
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
            }

            if (!player.isSneaking()) {
                NoTreePunching.log.info("Opening Vessel GUI");
                player.openGui(NoTreePunching.instance, ModGuiHandler.SMALL_VESSEL, world, 0, 0, 0);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemStackInventoryWrapper(stack, nbt, 4);
    }
}
