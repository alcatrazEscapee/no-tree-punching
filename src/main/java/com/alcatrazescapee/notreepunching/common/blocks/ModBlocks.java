/*
 * Part of the No Tree Punching mod by AlcatrazEscapee.
 * Work under copyright. See the project LICENSE.md for details.
 */

package com.alcatrazescapee.notreepunching.common.blocks;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.minecraft.core.Registry;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.items.LargeVesselBlockItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;
import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

@SuppressWarnings("unused")
public final class ModBlocks
{
    public static final RegistryInterface<Block> BLOCKS = XPlatform.INSTANCE.registryInterface(Registry.BLOCK);

    public static final RegistryHolder<Block> ANDESITE_COBBLESTONE = register("andesite_cobblestone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F, 6.0F)));
    public static final RegistryHolder<Block> DIORITE_COBBLESTONE = register("diorite_cobblestone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F, 6.0F)));
    public static final RegistryHolder<Block> GRANITE_COBBLESTONE = register("granite_cobblestone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F, 6.0F)));

    public static final RegistryHolder<StairBlock> ANDESITE_COBBLESTONE_STAIRS = register("andesite_cobblestone_stairs", () -> new StairBlock(() -> ANDESITE_COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryHolder<StairBlock> DIORITE_COBBLESTONE_STAIRS = register("diorite_cobblestone_stairs", () -> new StairBlock(() -> DIORITE_COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryHolder<StairBlock> GRANITE_COBBLESTONE_STAIRS = register("granite_cobblestone_stairs", () -> new StairBlock(() -> GRANITE_COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));

    public static final RegistryHolder<SlabBlock> ANDESITE_COBBLESTONE_SLAB = register("andesite_cobblestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryHolder<SlabBlock> DIORITE_COBBLESTONE_SLAB = register("diorite_cobblestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryHolder<SlabBlock> GRANITE_COBBLESTONE_SLAB = register("granite_cobblestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));

    public static final RegistryHolder<WallBlock> ANDESITE_COBBLESTONE_WALL = register("andesite_cobblestone_wall", () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryHolder<WallBlock> DIORITE_COBBLESTONE_WALL = register("diorite_cobblestone_wall", () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryHolder<WallBlock> GRANITE_COBBLESTONE_WALL = register("granite_cobblestone_wall", () -> new WallBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f, 6.0f)));

    public static final RegistryHolder<LooseRockBlock> ANDESITE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryHolder<LooseRockBlock> DIORITE_LOOSE_ROCK = register("diorite_loose_rock", LooseRockBlock::new);
    public static final RegistryHolder<LooseRockBlock> GRANITE_LOOSE_ROCK = register("granite_loose_rock", LooseRockBlock::new);
    public static final RegistryHolder<LooseRockBlock> STONE_LOOSE_ROCK = register("stone_loose_rock", LooseRockBlock::new);
    public static final RegistryHolder<LooseRockBlock> SANDSTONE_LOOSE_ROCK = register("sandstone_loose_rock", LooseRockBlock::new);
    public static final RegistryHolder<LooseRockBlock> RED_SANDSTONE_LOOSE_ROCK = register("red_sandstone_loose_rock", LooseRockBlock::new);

    public static final Map<PotteryBlock.Variant, RegistryHolder<PotteryBlock>> POTTERY = Arrays.stream(PotteryBlock.Variant.values()).collect(Collectors.toMap(Function.identity(), pottery -> register("clay_" + pottery.name().toLowerCase(), () -> new PotteryBlock(pottery))));

    public static final RegistryHolder<LargeVesselBlock> CERAMIC_LARGE_VESSEL = register("ceramic_large_vessel", LargeVesselBlock::new, block -> new LargeVesselBlockItem(block, new Item.Properties().tab(ModItemGroup.ITEMS)));

    private static <T extends Block> RegistryHolder<T> register(String name, Supplier<T> blockFactory)
    {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties().tab(ModItemGroup.ITEMS)));
    }

    private static <T extends Block> RegistryHolder<T> register(String name, Supplier<T> blockFactory, Function<T, BlockItem> blockItemFactory)
    {
        final RegistryHolder<T> block = BLOCKS.register(name, blockFactory);
        ModItems.ITEMS.register(name, () -> blockItemFactory.apply(block.get()));
        return block;
    }
}