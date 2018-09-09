/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

import com.alcatrazescapee.alcatrazcore.item.ItemArmorCore;
import com.alcatrazescapee.alcatrazcore.item.tool.*;
import com.alcatrazescapee.notreepunching.common.items.ItemKnife;
import com.alcatrazescapee.notreepunching.common.items.ItemMattock;
import com.alcatrazescapee.notreepunching.common.items.ItemSaw;

import static com.alcatrazescapee.notreepunching.common.ModMaterials.*;

public enum Metal
{
    IRON(true, Item.ToolMaterial.IRON, null),
    GOLD(true, Item.ToolMaterial.GOLD, null),
    DIAMOND(true, Item.ToolMaterial.DIAMOND, null),
    COPPER(false, TOOL_COPPER, ARMOR_COPPER),
    TIN(false, TOOL_TIN, ARMOR_TIN),
    BRONZE(false, TOOL_BRONZE, ARMOR_BRONZE),
    STEEL(false, TOOL_STEEL, ARMOR_STEEL);

    public final boolean isDefault;
    public final Item.ToolMaterial toolMaterial;
    public final ItemArmor.ArmorMaterial armorMaterial;

    Metal(boolean isDefault, @Nullable Item.ToolMaterial toolMaterial, @Nullable ItemArmor.ArmorMaterial armorMaterial)
    {
        this.isDefault = isDefault;
        this.toolMaterial = toolMaterial;
        this.armorMaterial = armorMaterial;
    }

    public enum ToolType
    {
        PICKAXE(true, true),
        HOE(true, true),
        SWORD(true, false),
        AXE(true, true),
        SHOVEL(true, true),
        MATTOCK(false, false),
        KNIFE(false, true),
        SAW(false, false);

        public final boolean isDefault;
        public final boolean isFlint;

        ToolType(boolean isDefault, boolean isFlint)
        {
            this.isDefault = isDefault;
            this.isFlint = isFlint;
        }

        @Nonnull
        public Item create(@Nonnull Item.ToolMaterial toolMaterial)
        {
            switch (this)
            {
                case PICKAXE:
                    return new ItemPickCore(toolMaterial);
                case HOE:
                    return new ItemHoeCore(toolMaterial);
                case SWORD:
                    return new ItemSwordCore(toolMaterial);
                case AXE:
                    return new ItemAxeCore(toolMaterial);
                case SHOVEL:
                    return new ItemSpadeCore(toolMaterial);
                case MATTOCK:
                    return new ItemMattock(toolMaterial);
                case SAW:
                    return new ItemSaw(toolMaterial);
                default: // KNIFE
                    return new ItemKnife(toolMaterial);
            }
        }

        @Nonnull
        @SuppressWarnings("ConstantConditions")
        public Item createFlint()
        {
            switch (this)
            {
                default:
                    return create(TOOL_FLINT);
            }
        }
    }

    public enum ArmorType
    {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS;

        public ItemArmor create(@Nonnull ItemArmor.ArmorMaterial armorMaterial)
        {
            switch (this)
            {
                case HELMET:
                    return new ItemArmorCore(armorMaterial, EntityEquipmentSlot.HEAD);
                case CHESTPLATE:
                    return new ItemArmorCore(armorMaterial, EntityEquipmentSlot.CHEST);
                case LEGGINGS:
                    return new ItemArmorCore(armorMaterial, EntityEquipmentSlot.LEGS);
                default: // BOOTS
                    return new ItemArmorCore(armorMaterial, EntityEquipmentSlot.FEET);
            }
        }
    }
}
