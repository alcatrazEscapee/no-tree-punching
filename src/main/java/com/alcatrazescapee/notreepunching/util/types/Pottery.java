/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util.types;

import javax.annotation.Nullable;

public enum Pottery
{
    WORKED,
    LARGE_VESSEL,
    SMALL_VESSEL,
    BUCKET,
    FLOWER_POT;

    private static Pottery[] values = values();

    @Nullable
    public Pottery next()
    {
        if (this == FLOWER_POT) return null;
        return values[this.ordinal() + 1];
    }
}
