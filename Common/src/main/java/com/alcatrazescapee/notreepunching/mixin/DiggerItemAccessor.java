package com.alcatrazescapee.notreepunching.mixin;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DiggerItem.class)
public interface DiggerItemAccessor
{
    @Accessor("blocks")
    TagKey<Block> getBlocks();
}
