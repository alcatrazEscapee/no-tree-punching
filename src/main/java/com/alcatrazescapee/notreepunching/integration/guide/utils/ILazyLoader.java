/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.integration.guide.utils;

import amerifrance.guideapi.api.impl.Book;

public interface ILazyLoader
{
    static void init(Book book)
    {
        book.getCategoryList().forEach(cat ->
                cat.entries.values().forEach(ent ->
                        ent.pageList.forEach(page ->
                        {
                            if (page instanceof ILazyLoader)
                            {
                                ((ILazyLoader) page).loadPost();
                            }
                        })));
    }

    void loadPost();
}
