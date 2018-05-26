package notreepunching.block.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;

public interface IHasTESR<TE extends TileEntity, TESR extends TileEntitySpecialRenderer> extends IHasTileEntity<TE>{

    @Nonnull
    TESR getTESR();

}
