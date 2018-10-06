/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.guide.utils;

import net.minecraft.util.ResourceLocation;

import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Entry;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.page.PageText;

public class CategoryBuilder
{
    private CategoryAbstract rootCategory;
    private EntryAbstract lastEntry;

    public CategoryBuilder(CategoryAbstract rootCategory)
    {
        this.rootCategory = rootCategory;
    }

    public CategoryBuilder addEntry(String name)
    {
        lastEntry = new Entry(name);
        rootCategory.addEntry(lastEntry.name, lastEntry);
        return this;
    }

    public CategoryBuilder addPage(IPage page)
    {
        lastEntry.addPage(page);
        return this;
    }

    public CategoryBuilder addPage(ResourceLocation recipeLookup)
    {
        lastEntry.addPage(new PageIRecipeLazy(recipeLookup));
        return this;
    }

    public CategoryBuilder addPage(String text)
    {
        lastEntry.addPage(new PageText(text));
        return this;
    }

    public CategoryAbstract build()
    {
        return rootCategory;
    }
}
