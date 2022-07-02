package com.alcatrazescapee.notreepunching.platform;

import java.util.ServiceLoader;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.recipes.RecipeSerializerImpl;

public interface XPlatform
{
    XPlatform INSTANCE = find(XPlatform.class);

    static <T> T find(Class<T> clazz)
    {
        return ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }

    <T> RegistryInterface<T> registryInterface(Registry<T> registry);

    Config createConfig();

    CreativeModeTab creativeTab(ResourceLocation id, Supplier<ItemStack> icon);

    Tier toolTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, TagKey<Block> tag, Supplier<Ingredient> repairIngredient);

    StairBlock stairBlock(Supplier<BlockState> state, BlockBehaviour.Properties properties);

    // todo: default, return impl
    <T extends Recipe<?>> RecipeSerializer<T> recipeSerializer(RecipeSerializerImpl<T> impl);

    default void openScreen(ServerPlayer serverPlayer, MenuProvider provider, BlockPos pos)
    {
        openScreen(serverPlayer, provider, buffer -> buffer.writeBlockPos(pos));
    }

    void openScreen(ServerPlayer serverPlayer, MenuProvider provider, Consumer<FriendlyByteBuf> buffer);

    boolean isDedicatedClient();
}
