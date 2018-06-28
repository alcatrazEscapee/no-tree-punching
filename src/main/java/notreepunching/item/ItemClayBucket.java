/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.items.ItemHandlerHelper;
import notreepunching.NoTreePunching;
import notreepunching.client.ModTabs;
import notreepunching.config.ModConfig;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemClayBucket extends UniversalBucket{

    public final String name;

    ItemClayBucket(String name){
        super(Fluid.BUCKET_VOLUME, ItemStack.EMPTY, false);
        this.name = name;

        register();
    }

    public void register(){
        ModItems.addItemToRegistry(this,name, ModTabs.ITEMS_TAB);
        NoTreePunching.proxy.addModelToRegistry(new ItemStack(this), this.getRegistryName(), "inventory");
    }

    @Override
    public void getSubItems(@Nullable final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
        if (!this.isInCreativeTab(tab)) return;

        subItems.add(new ItemStack(this));

        for (final Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
            // Add all fluids that the bucket can be filled with
            final FluidStack fs = new FluidStack(fluid, getCapacity());
            final ItemStack stack = new ItemStack(this);
            final IFluidHandlerItem fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
            if (fluidHandler != null && fluidHandler.fill(fs, true) == fs.amount) {
                final ItemStack filled = fluidHandler.getContainer();
                subItems.add(filled);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        /*final FluidStack fluidStack = getFluid(stack);
        final String unlocalisedName = this.getUnlocalizedNameInefficiently(stack);

        // If the bucket is empty, translate the unlocalised name directly
        if (fluidStack == null) {
            return NoTreePunching.proxy.localize(unlocalisedName + ".name");
        }
        return NoTreePunching.proxy.localize(unlocalisedName + "." + fluidStack.getFluid().getName() + ".name");

        // If there's a fluid-specific translation, use it
        //final String fluidUnlocalisedName = unlocalisedName + ".filled." + fluidStack.getFluid().getName() + ".name";
        //return NoTreePunching.proxy.localize(fluidUnlocalisedName);*/

        final FluidStack fluidStack = getFluid(stack);
        final String unlocalisedName = this.getUnlocalizedNameInefficiently(stack);

        // If the bucket is empty, translate the unlocalised name directly
        if (fluidStack == null) {
            return I18n.translateToLocal(unlocalisedName + ".name").trim();
        }

        // If there's a fluid-specific translation, use it
        final String fluidUnlocalisedName = unlocalisedName + ".filled." + fluidStack.getFluid().getName() + ".name";
        if (I18n.canTranslate(fluidUnlocalisedName)) {
            return I18n.translateToLocal(fluidUnlocalisedName);
        }

        // Else translate the filled name directly, formatting it with the fluid name
        return I18n.translateToLocalFormatted(unlocalisedName + ".filled.name", fluidStack.getLocalizedName());
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
        final ItemStack heldItem = player.getHeldItem(hand);
        final FluidStack fluidStack = getFluid(heldItem);

        // If the bucket is full, call the super method to try and empty it
        if (fluidStack != null) return super.onItemRightClick(world, player, hand);

        // If the bucket is empty, try and fill it
        final RayTraceResult target = this.rayTrace(world, player, true);

        if (target == null || target.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, heldItem);
        }

        final BlockPos pos = target.getBlockPos();

        final ItemStack singleBucket = heldItem.copy();
        singleBucket.setCount(1);

        final FluidActionResult filledResult = FluidUtil.tryPickUpFluid(singleBucket, player, world, pos, target.sideHit);
        if (filledResult.isSuccess()) {
            final ItemStack filledBucket = filledResult.result;

            if (player.capabilities.isCreativeMode)
                return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);

            heldItem.shrink(1);
            if (heldItem.isEmpty())
                return new ActionResult<>(EnumActionResult.SUCCESS, filledBucket);

            ItemHandlerHelper.giveItemToPlayer(player, filledBucket);

            return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
        }

        return new ActionResult<>(EnumActionResult.PASS, heldItem);
    }

    @Override
    public ItemStack getEmpty() {
        return new ItemStack(this);
    }

    @Nullable
    @Override
    public FluidStack getFluid(final ItemStack container) {
        return FluidUtil.getFluidContained(container);
    }

    @Override
    public ICapabilityProvider initCapabilities(final ItemStack stack, final NBTTagCompound nbt) {
        // FluidBucketWrapper only works with Forge's UniversalBucket instance, use a different IFluidHandlerItem implementation instead
        return new FluidHandlerItemStackSimple(stack, Fluid.BUCKET_VOLUME) {
            @Override
            public boolean canFillFluidType(final FluidStack fluid) {
                for (String s : ModConfig.Pottery.CLAY_BUCKET_FLUIDS) {
                    if (s.equals(fluid.getFluid().getName())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
