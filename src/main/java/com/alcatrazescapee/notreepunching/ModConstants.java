/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching;

import java.util.Random;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

@SuppressWarnings({"WeakerAccess", "unused"})
public final class ModConstants
{
    public static final String MOD_ID = "notreepunching";
    public static final String MOD_NAME = "No Tree Punching";

    // Versioning
    private static final String FORGE_MIN = "14.23.4.2705";
    private static final String FORGE_MAX = "15.0.0.0";

    private static final String ALC_MIN = "1.0.0";
    private static final String ALC_MAX = "2.0.0";

    public static final String VERSION = "GRADLE:VERSION";
    public static final String DEPENDENCIES = "required-after:forge@[" + FORGE_MIN + "," + FORGE_MAX + ");required-after:alcatrazcore@[" + ALC_MIN + "," + ALC_MAX + ");";

    // Utility
    public static final Random RNG = new Random();
    public static final Converter<String, String> CASE_CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
}
