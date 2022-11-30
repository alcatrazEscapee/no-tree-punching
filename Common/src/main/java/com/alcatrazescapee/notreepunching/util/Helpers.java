package com.alcatrazescapee.notreepunching.util;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.util.inventory.ItemStackInventory;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

/**
 * A collection of helper methods and utilities
 */
public final class Helpers
{
    public static ResourceLocation identifier(String path)
    {
        return new ResourceLocation(NoTreePunching.MOD_ID, path);
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o)
    {
        return (T) o;
    }

    /**
     * A simplification of {@link ItemStack#hurtAndBreak(int, LivingEntity, Consumer)} that does not require an entity
     */
    public static ItemStack hurtAndBreak(ItemStack stack, int amount)
    {
        if (stack.isDamageableItem())
        {
            if (stack.hurt(amount, new XoroshiroRandomSource(System.currentTimeMillis()), null))
            {
                stack.shrink(1);
                stack.setDamageValue(0);
            }
        }
        return stack;
    }

    /**
     * Default argument, and allows a null player
     */
    public static ItemStack hurtAndBreak(@Nullable Player player, @Nullable InteractionHand hand, ItemStack stack, int amount)
    {
        if (player != null && hand != null)
        {
            stack.hurtAndBreak(amount, player, entity -> entity.broadcastBreakEvent(hand));
            return stack;
        }
        else
        {
            return Helpers.hurtAndBreak(stack, amount);
        }
    }

    public static boolean isItem(Item item, TagKey<Item> tag)
    {
        return item.builtInRegistryHolder().is(tag);
    }

    public static boolean isBlock(Block block, TagKey<Block> tag)
    {
        return block.builtInRegistryHolder().is(tag);
    }

    public static void giveItemToPlayer(Level level, Player player, ItemStack stack)
    {
        if (!stack.isEmpty() && !level.isClientSide)
        {
            final ItemEntity entity = new ItemEntity(level, player.getX(), player.getY() + 0.5, player.getZ(), stack);
            entity.setPickUpDelay(0);
            level.addFreshEntity(entity);
        }
    }

    /**
     * Copy pasta from {@link net.minecraft.world.level.block.ShulkerBoxBlock#appendHoverText(ItemStack, BlockGetter, List, TooltipFlag)}
     */
    public static void addInventoryTooltip(ItemStackInventory inventory, List<Component> tooltip)
    {
        int displayCount = 0;
        int totalCount = 0;

        for (int i = 0; i < inventory.size(); i++)
        {
            ItemStack contentStack = inventory.get(i);
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
            MutableComponent textComponent = Component.translatable(MOD_ID + ".tooltip.small_vessel_more", totalCount - displayCount);
            textComponent.setStyle(textComponent.getStyle().applyFormat(ChatFormatting.ITALIC));
            tooltip.add(textComponent);
        }
    }
}