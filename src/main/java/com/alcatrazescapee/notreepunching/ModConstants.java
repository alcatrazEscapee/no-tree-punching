/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import java.util.Random;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ModConstants
{
    public static final String MOD_ID = "notreepunching";
    public static final String MOD_NAME = "No Tree Punching";

    // Versioning / Dependencies
    public static final String VERSION = "GRADLE:VERSION";
    public static final String FORGE_REQUIRED = "required-after:forge@[GRADLE:FORGE_VERSION,15.0.0.0);";
    public static final String ALC_CORE_REQUIRED = "required-after:alcatrazcore@[0.1.4,2.0.0);";
    public static final String DEPENDENCIES = FORGE_REQUIRED + ALC_CORE_REQUIRED;

    // Utility
    public static final Random RNG = new Random();
}
