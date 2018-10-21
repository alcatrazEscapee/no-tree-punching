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

    // Versioning / Dependencies
    public static final String VERSION = "GRADLE:VERSION";
    public static final String FORGE_REQUIRED = "required-after:forge@[14.23.4.2705,15.0.0.0);";
    public static final String ALC_CORE_REQUIRED = "required-after:alcatrazcore@[GRADLE:ALC_CORE_VERSION,2.0.0);";
    public static final String DEPENDENCIES = FORGE_REQUIRED + ALC_CORE_REQUIRED;

    // Utility
    public static final Random RNG = new Random();
    public static final Converter<String, String> CASE_CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
}
