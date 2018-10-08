/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.guide;

import java.awt.*;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.BookBinder;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.page.PageTextImage;
import com.alcatrazescapee.notreepunching.common.blocks.BlockPottery;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.integration.guide.utils.CategoryBuilder;
import com.alcatrazescapee.notreepunching.integration.guide.utils.ILazyLoader;
import com.alcatrazescapee.notreepunching.integration.guide.utils.PageFurnaceRecipeLazy;
import com.alcatrazescapee.notreepunching.util.types.Pottery;
import com.alcatrazescapee.notreepunching.util.types.ToolType;

import static com.alcatrazescapee.notreepunching.ModConstants.MOD_ID;
import static com.alcatrazescapee.notreepunching.client.ModTabs.TAB_ITEMS;

@GuideBook
@SuppressWarnings("unused")
public final class ModGuideBook implements IGuideBook
{
    private Book book;

    @Nullable
    @Override
    public Book buildBook()
    {
        book = new BookBinder(new ResourceLocation(MOD_ID, "guide_book"))
                .addCategory(new CategoryBuilder(new CategoryItemStack(MOD_ID + ".guide.category.intro", new ItemStack(ModItems.getFlintTool(ToolType.KNIFE))).withKeyBase("intro"))
                        .addEntry(MOD_ID + ".guide.intro.overview")
                        .addPage(new PageTextImage(MOD_ID + ".guide.intro.page.1", new ResourceLocation(MOD_ID, "textures/guide/splash.png"), false))
                        .addPage(MOD_ID + ".guide.intro.page.2")
                        .addEntry(MOD_ID + ".guide.intro.block_breaking")
                        .addPage(MOD_ID + ".guide.intro.page.3")
                        .addPage(MOD_ID + ".guide.intro.page.4")
                        .addEntry(MOD_ID + ".guide.intro.world_gen")
                        .addPage(new PageTextImage(MOD_ID + ".guide.intro.page.5", new ResourceLocation(MOD_ID, "textures/guide/loose_rocks.png"), false))
                        .addPage(MOD_ID + ".guide.intro.page.6")
                        .addEntry(MOD_ID + ".guide.intro.mechanics")
                        .addPage(MOD_ID + ".guide.intro.page.7")
                        .build())
                .addCategory(new CategoryBuilder(new CategoryItemStack(MOD_ID + ".guide.category.early_game", new ItemStack(ModItems.FLINT_SHARD)).withKeyBase("early_game"))
                        .addEntry(MOD_ID + ".guide.early_game.sticks")
                        .addPage(MOD_ID + ".guide.early_game.page.1")
                        .addEntry(MOD_ID + ".guide.early_game.stones")
                        .addPage(MOD_ID + ".guide.early_game.page.2")
                        .addPage(new ResourceLocation(MOD_ID, "blocks/cobblestone_stone"))
                        .addPage(new ResourceLocation(MOD_ID, "blocks/cobblestone_andesite"))
                        .addPage(new ResourceLocation(MOD_ID, "blocks/cobblestone_diorite"))
                        .addPage(new ResourceLocation(MOD_ID, "blocks/cobblestone_granite"))
                        .addPage(new ResourceLocation(MOD_ID, "blocks/rocks_to_sandstone"))
                        .addEntry(MOD_ID + ".guide.early_game.knapping")
                        .addPage(new PageTextImage(MOD_ID + ".guide.early_game.page.3", new ResourceLocation(MOD_ID, "textures/guide/knapping.png"), false))
                        .addPage(MOD_ID + ".guide.early_game.page.4")
                        .addEntry(MOD_ID + ".guide.early_game.knife")
                        .addPage(MOD_ID + ".guide.early_game.page.5")
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_knife"))
                        .addEntry(MOD_ID + ".guide.early_game.plant_fiber")
                        .addPage(MOD_ID + ".guide.early_game.page.6")
                        .addPage(new ResourceLocation(MOD_ID, "misc/grass_string"))
                        .addEntry(MOD_ID + ".guide.early_game.hatchet")
                        .addPage(MOD_ID + ".guide.early_game.page.7")
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_axe"))
                        .addEntry(MOD_ID + ".guide.early_game.chopping")
                        .addPage(MOD_ID + ".guide.early_game.page.8")
                        .addEntry(MOD_ID + ".guide.early_game.fire_pit")
                        .addPage(MOD_ID + ".guide.early_game.page.9")
                        .addPage(new ResourceLocation(MOD_ID, "tools/fire_starter"))
                        .addPage(MOD_ID + ".guide.early_game.page.10")
                        .addPage(new PageTextImage(MOD_ID + ".guide.early_game.page.11", new ResourceLocation(MOD_ID, "textures/guide/fire_pit.png"), false))
                        .addPage(MOD_ID + ".guide.early_game.page.12")
                        .build())
                .addCategory(new CategoryBuilder(new CategoryItemStack(MOD_ID + ".guide.category.tools", new ItemStack(ModItems.FIRE_STARTER)).withKeyBase("tools"))
                        .addEntry(MOD_ID + ".guide.tools.flint")
                        .addPage(MOD_ID + ".guide.tools.page.1")
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_knife"))
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_axe"))
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_pickaxe"))
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_shovel"))
                        .addPage(new ResourceLocation(MOD_ID, "tools/flint_hoe"))
                        .addEntry(MOD_ID + ".guide.tools.knife")
                        .addPage(MOD_ID + ".guide.tools.page.2")
                        .addPage(new ResourceLocation(MOD_ID, "tools/iron_knife"))
                        .addEntry(MOD_ID + ".guide.tools.saw")
                        .addPage(MOD_ID + ".guide.tools.page.3")
                        .addPage(new ResourceLocation(MOD_ID, "tools/iron_saw"))
                        .addEntry(MOD_ID + ".guide.tools.mattock")
                        .addPage(MOD_ID + ".guide.tools.page.4")
                        .addPage(new ResourceLocation(MOD_ID, "tools/iron_mattock"))
                        .addEntry(MOD_ID + ".guide.tools.fire_starter")
                        .addPage(MOD_ID + ".guide.tools.page.5")
                        .addPage(new ResourceLocation(MOD_ID, "tools/fire_starter"))
                        .build())
                .addCategory(new CategoryBuilder(new CategoryItemStack(MOD_ID + ".guide.category.pottery", new ItemStack(ModItems.CLAY_TOOL)).withKeyBase("pottery"))
                        .addEntry(MOD_ID + ".guide.pottery.intro")
                        .addPage(MOD_ID + ".guide.pottery.page.1")
                        .addPage(MOD_ID + ".guide.pottery.page.2")
                        .addPage(MOD_ID + ".guide.pottery.page.3")
                        .addEntry(MOD_ID + ".guide.pottery.clay_brick")
                        .addPage(MOD_ID + ".guide.pottery.page.4")
                        .addPage(new ResourceLocation(MOD_ID, "misc/clay_tool_clay_brick"))
                        .addPage(new ResourceLocation(MOD_ID, "misc/clay_tool_clay_brick_2"))
                        .addPage(new PageFurnaceRecipeLazy(ModItems.CLAY_BRICK))
                        .addEntry(MOD_ID + ".guide.pottery.large_vessel")
                        .addPage(MOD_ID + ".guide.pottery.page.5")
                        .addPage(new PageFurnaceRecipeLazy(BlockPottery.get(Pottery.LARGE_VESSEL)))
                        .addEntry(MOD_ID + ".guide.pottery.small_vessel")
                        .addPage(MOD_ID + ".guide.pottery.page.6")
                        .addPage(new PageFurnaceRecipeLazy(BlockPottery.get(Pottery.SMALL_VESSEL)))
                        .addEntry(MOD_ID + ".guide.pottery.bucket")
                        .addPage(MOD_ID + ".guide.pottery.page.7")
                        .addPage(new PageFurnaceRecipeLazy(BlockPottery.get(Pottery.BUCKET)))
                        .addEntry(MOD_ID + ".guide.pottery.flower_pot")
                        .addPage(MOD_ID + ".guide.pottery.page.8")
                        .addPage(new PageFurnaceRecipeLazy(BlockPottery.get(Pottery.FLOWER_POT)))
                        .build())
                .setGuideTitle(MOD_ID + ".guide.title")
                .setHeader(MOD_ID + ".guide.title")
                .setItemName(MOD_ID + ".guide.item_name")
                .setAuthor("AlcatrazEscapee")
                .setColor(Color.CYAN)
                .setSpawnWithBook()
                .setCreativeTab(TAB_ITEMS)
                .build();
        return book;
    }

    @Nonnull
    @Override
    public IRecipe getRecipe(@Nonnull ItemStack bookStack)
    {
        // This needs to use an alternate mod id otherwise it gets 'dangerous alternative prefix' warnings
        ResourceLocation loc = new ResourceLocation("guideapi", "guide_book_recipe");
        return new ShapedOreRecipe(loc, bookStack, "SR", 'S', Items.BOOK, 'R', "rock").setRegistryName(loc);
    }

    @Override
    public void handlePost(@Nonnull ItemStack bookStack)
    {
        ILazyLoader.init(book);
    }

}
