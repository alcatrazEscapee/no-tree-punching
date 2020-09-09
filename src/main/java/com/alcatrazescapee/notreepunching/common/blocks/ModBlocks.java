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

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.alcatrazescapee.notreepunching.common.ModItemGroup;
import com.alcatrazescapee.notreepunching.common.items.LargeVesselBlockItem;
import com.alcatrazescapee.notreepunching.common.items.ModItems;

import static com.alcatrazescapee.notreepunching.NoTreePunching.MOD_ID;

@SuppressWarnings("unused")
public final class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static final RegistryObject<Block> ANDESITE_COBBLESTONE = register("andesite_cobblestone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> DIORITE_COBBLESTONE = register("diorite_cobblestone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> GRANITE_COBBLESTONE = register("granite_cobblestone", () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(2.0F, 6.0F)));

    public static final RegistryObject<StairsBlock> ANDESITE_COBBLESTONE_STAIRS = register("andesite_cobblestone_stairs", () -> new StairsBlock(() -> ANDESITE_COBBLESTONE.get().defaultBlockState(), AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryObject<StairsBlock> DIORITE_COBBLESTONE_STAIRS = register("diorite_cobblestone_stairs", () -> new StairsBlock(() -> DIORITE_COBBLESTONE.get().defaultBlockState(), AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryObject<StairsBlock> GRANITE_COBBLESTONE_STAIRS = register("granite_cobblestone_stairs", () -> new StairsBlock(() -> GRANITE_COBBLESTONE.get().defaultBlockState(), AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));

    public static final RegistryObject<SlabBlock> ANDESITE_COBBLESTONE_SLAB = register("andesite_cobblestone_slab", () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryObject<SlabBlock> DIORITE_COBBLESTONE_SLAB = register("diorite_cobblestone_slab", () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryObject<SlabBlock> GRANITE_COBBLESTONE_SLAB = register("granite_cobblestone_slab", () -> new SlabBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));

    public static final RegistryObject<WallBlock> ANDESITE_COBBLESTONE_WALL = register("andesite_cobblestone_wall", () -> new WallBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryObject<WallBlock> DIORITE_COBBLESTONE_WALL = register("diorite_cobblestone_wall", () -> new WallBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));
    public static final RegistryObject<WallBlock> GRANITE_COBBLESTONE_WALL = register("granite_cobblestone_wall", () -> new WallBlock(AbstractBlock.Properties.of(Material.STONE).strength(2.0f, 6.0f)));

    public static final RegistryObject<LooseRockBlock> ANDESITE_LOOSE_ROCK = register("andesite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> DIORITE_LOOSE_ROCK = register("diorite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> GRANITE_LOOSE_ROCK = register("granite_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> STONE_LOOSE_ROCK = register("stone_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> SANDSTONE_LOOSE_ROCK = register("sandstone_loose_rock", LooseRockBlock::new);
    public static final RegistryObject<LooseRockBlock> RED_SANDSTONE_LOOSE_ROCK = register("red_sandstone_loose_rock", LooseRockBlock::new);

    public static final Map<PotteryBlock.Variant, RegistryObject<PotteryBlock>> POTTERY = Arrays.stream(PotteryBlock.Variant.values()).collect(Collectors.toMap(Function.identity(), pottery -> register("clay_" + pottery.name().toLowerCase(), () -> new PotteryBlock(pottery))));

    public static final RegistryObject<LargeVesselBlock> CERAMIC_LARGE_VESSEL = register("ceramic_large_vessel", LargeVesselBlock::new, block -> new LargeVesselBlockItem(block, new Item.Properties().tab(ModItemGroup.ITEMS)));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockFactory)
    {
        return register(name, blockFactory, block -> new BlockItem(block, new Item.Properties().tab(ModItemGroup.ITEMS)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockFactory, Function<T, BlockItem> blockItemFactory)
    {
        RegistryObject<T> block = BLOCKS.register(name, blockFactory);
        ModItems.ITEMS.register(name, () -> blockItemFactory.apply(block.get()));
        return block;
    }
}