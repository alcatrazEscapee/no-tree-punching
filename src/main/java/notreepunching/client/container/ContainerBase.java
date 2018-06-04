package notreepunching.client.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import notreepunching.block.tile.IHasFields;
import notreepunching.block.tile.TileEntitySidedInventory;

import javax.annotation.Nonnull;

public abstract class ContainerBase<TE extends TileEntity> extends Container {

    private int [] cachedFields;

    protected TE tile;

    ContainerBase(InventoryPlayer playerInv, TE te) {
        this.tile = te;

        addContainerSlots(te);
        addPlayerInventorySlots(playerInv);
    }

    protected abstract void addContainerSlots(TE tile);

    private void addPlayerInventorySlots(InventoryPlayer playerInv){
        // Add Player Inventory Slots
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; k++) {
            addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }
    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if(tile instanceof IHasFields) {
            IHasFields te = (IHasFields) tile;
            boolean allFieldsHaveChanged = false;
            boolean fieldHasChanged[] = new boolean[te.getFieldCount()];
            if (cachedFields == null) {
                cachedFields = new int[te.getFieldCount()];
                allFieldsHaveChanged = true;
            }
            for (int i = 0; i < cachedFields.length; ++i) {
                if (allFieldsHaveChanged || cachedFields[i] != te.getField(i)) {
                    cachedFields[i] = te.getField(i);
                    fieldHasChanged[i] = true;
                }
            }

            // go through the list of listeners (players using this container) and update them if necessary
            for (IContainerListener listener : this.listeners) {
                for (int fieldID = 0; fieldID < te.getFieldCount(); ++fieldID) {
                    if (fieldHasChanged[fieldID]) {
                        // Note that although sendWindowProperty takes 2 ints on a server these are truncated to shorts
                        listener.sendWindowProperty(this, fieldID, cachedFields[fieldID]);
                    }
                }
            }
        }
    }

    // Called when a progress bar update is received from the server. The two values (id and data) are the same two
    // values given to sendWindowProperty.  In this case we are using fields so we just pass them to the tileEntity.
    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        if(tile instanceof IHasFields) {
            ((IHasFields)tile).setField(id, data);
        }
    }

}
