/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package notreepunching.common.capability;

import net.minecraft.item.ItemStack;

public interface IPlayerHarvestTool
{
    ItemStack getStack();

    void setStack(ItemStack stack);
}
