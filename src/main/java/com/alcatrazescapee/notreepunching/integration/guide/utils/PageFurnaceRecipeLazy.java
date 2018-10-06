/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.guide.utils;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.FurnaceRecipes;

import amerifrance.guideapi.page.PageFurnaceRecipe;

public class PageFurnaceRecipeLazy extends PageFurnaceRecipe implements ILazyLoader
{
    public PageFurnaceRecipeLazy(Block block)
    {
        super(block);
    }

    public PageFurnaceRecipeLazy(Item item)
    {
        super(item);
    }

    @Override
    public void loadPost()
    {
        this.output = FurnaceRecipes.instance().getSmeltingResult(input);
    }
}
