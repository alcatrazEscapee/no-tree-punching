/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util.types;

import java.util.Random;
import javax.annotation.Nonnull;

public enum Stone
{
    STONE(true),
    ANDESITE(true),
    GRANITE(true),
    DIORITE(true);

    private static Stone[] values = values();

    @Nonnull
    public static Stone getRandom(Random random)
    {
        return values[random.nextInt(values.length)];
    }

    public final boolean isEnabled;

    Stone(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
    }
}
