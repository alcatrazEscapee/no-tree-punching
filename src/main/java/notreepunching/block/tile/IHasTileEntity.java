/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.block.tile;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<TE extends TileEntity> {

    Class<TE> getTileEntityClass();
}
