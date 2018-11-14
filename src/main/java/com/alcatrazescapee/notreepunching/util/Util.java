/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import java.util.Random;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;

public final class Util
{
    public static final Random RNG = new Random();
    public static final Converter<String, String> CASE_CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
}
