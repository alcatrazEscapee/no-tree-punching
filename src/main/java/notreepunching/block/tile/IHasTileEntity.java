package notreepunching.block.tile;

import net.minecraft.tileentity.TileEntity;

public interface IHasTileEntity<TE extends TileEntity> {

    Class<TE> getTileEntityClass();
}
