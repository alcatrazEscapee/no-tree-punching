/*
 *  Part of the No Tree Punching Mod by alcatrazEscapee
 *  Work under Copyright. Licensed under the GPL-3.0.
 *  See the project LICENSE.md for more information.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import com.alcatrazescapee.notreepunching.common.ModItemGroups;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

public final class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> ANDESITE_COBBLESTONE = register("andesite_cobblestone", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F)));
    public static final RegistryObject<Block> DIORITE_COBBLESTONE = register("diorite_cobblestone", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F)));
    public static final RegistryObject<Block> GRANITE_COBBLESTONE = register("granite_cobblestone", () -> new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.0F, 6.0F)));

    public static final RegistryObject<LooseRockBlock> ANDESITE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> DIORITE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> GRANITE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> STONE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> SANDSTONE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> RED_SANDSTONE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);

    public static final Map<PotteryBlock.Variant, RegistryObject<PotteryBlock>> POTTERY = Arrays.stream(PotteryBlock.Variant.values()).collect(Collectors.toMap(Function.identity(), pottery -> register("clay_" + pottery.name().toLowerCase(), () -> new PotteryBlock(pottery))));

    public static final RegistryObject<LargeVesselBlock> CERAMIC_LARGE_VESSEL = register("ceramic_large_vessel", () -> new LargeVesselBlock())

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockFactory)
    {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties().group(ModItemGroups.ITEMS)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockFactory, Function<T, BlockItem> blockItemFactory)
    {
        RegistryObject<T> block = BLOCKS.register(name, blockFactory);
        ModItems.ITEMS.register(name, () -> blockItemFactory.apply(block.get()));
        return block;
    }
}
