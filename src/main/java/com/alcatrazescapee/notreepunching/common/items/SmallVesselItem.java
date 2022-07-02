package com.alcatrazescapee.notreepunching.common.items;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

import com.alcatrazescapee.notreepunching.common.ModTags;
import com.alcatrazescapee.notreepunching.common.container.SmallVesselContainer;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import com.alcatrazescapee.notreepunching.util.Helpers;
import com.alcatrazescapee.notreepunching.util.inventory.ItemStackAttachedInventory;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public class SmallVesselItem extends Item
{
    public static final int SLOT_ROWS = 3, SLOT_COLUMNS = 3, SLOTS = SLOT_ROWS * SLOT_COLUMNS;
    public static final ItemStackAttachedInventory.Factory INVENTORY = ItemStackAttachedInventory.create(SLOTS, stack -> !Helpers.isItem(stack.getItem(), ModTags.Items.SMALL_VESSEL_BLACKLIST));

    public SmallVesselItem()
    {
        super(new Properties().tab(ModItems.Tab.ITEMS).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        final ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayer serverPlayer && !player.isShiftKeyDown())
        {
            XPlatform.INSTANCE.openScreen(
                serverPlayer,
                new SimpleMenuProvider((windowID, playerInventory, playerIn) -> new SmallVesselContainer(windowID, playerInventory, hand), stack.getHoverName()),
                buffer -> buffer.writeBoolean(hand == InteractionHand.MAIN_HAND)
            );
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    /**
     * Copy pasta from {@link net.minecraft.world.level.block.ShulkerBoxBlock#appendHoverText(ItemStack, BlockGetter, List, TooltipFlag)}
     */
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
    {
        final CompoundTag tag = stack.getTag();
        if (tag != null)
        {
            final ItemStackAttachedInventory inventory = INVENTORY.create(stack);

            int displayCount = 0;
            int totalCount = 0;
            for (int i = 0; i < inventory.size(); i++)
            {
                final ItemStack contentStack = inventory.get(i);
                if (!contentStack.isEmpty())
                {
                    ++totalCount;
                    if (displayCount <= 4)
                    {
                        ++displayCount;
                        MutableComponent textComponent = contentStack.getHoverName().plainCopy();
                        textComponent.append(" x").append(String.valueOf(contentStack.getCount()));
                        tooltip.add(textComponent);
                    }
                }
            }

            if (totalCount > displayCount)
            {
                MutableComponent textComponent = new TranslatableComponent(MOD_ID + ".tooltip.small_vessel_more", totalCount - displayCount);
                textComponent.setStyle(textComponent.getStyle().applyFormat(ChatFormatting.ITALIC));
                tooltip.add(textComponent);
            }
        }
    }
}