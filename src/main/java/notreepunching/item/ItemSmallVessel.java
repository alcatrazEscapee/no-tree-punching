package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import notreepunching.NoTreePunching;
import notreepunching.block.tile.inventory.ItemHandlerInventoryItem;
import notreepunching.block.tile.inventory.ItemStackInventoryWrapper;
import notreepunching.client.ModGuiHandler;

import javax.annotation.Nonnull;
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
                System.out.println("Opening the gui peeps");
                player.openGui(NoTreePunching.instance, ModGuiHandler.SMALL_VESSEL, world, 0, 0, 0);
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    public static ItemStack setDefaultInventoryTag(ItemStack container) {
        if (container.getTagCompound() == null) {
            container.setTagCompound(new NBTTagCompound());
        }
        return container;
    }

    public static boolean needsTag(ItemStack container) {
        return container.getTagCompound() == null;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new ItemStackInventoryWrapper(stack, nbt, 4);
    }
}
