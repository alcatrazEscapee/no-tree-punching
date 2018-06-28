/*
 *
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 *
 */

package notreepunching.block.tile;

public interface IHasFields {

    int getField(int id);

    void setField(int id, int value);

    int getFieldCount();
}
