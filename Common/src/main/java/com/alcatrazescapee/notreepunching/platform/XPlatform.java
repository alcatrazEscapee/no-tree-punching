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
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.items.CeramicBucketItem;
import com.alcatrazescapee.notreepunching.common.items.MattockItem;
import com.alcatrazescapee.notreepunching.common.recipes.RecipeSerializerImpl;
import com.alcatrazescapee.notreepunching.common.recipes.ToolDamagingRecipe;
import com.alcatrazescapee.notreepunching.platform.event.BlockEntityFactory;
import com.alcatrazescapee.notreepunching.platform.event.ContainerFactory;

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

    // Constructors / Misc. Objects

    CreativeModeTab creativeTab(ResourceLocation id, Supplier<ItemStack> icon);

    Tier toolTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, TagKey<Block> tag, Supplier<Ingredient> repairIngredient);

    StairBlock stairBlock(Supplier<BlockState> state, BlockBehaviour.Properties properties);

    default CeramicBucketItem bucketItem(Fluid fluid, Item.Properties properties)
    {
        return new CeramicBucketItem(fluid, properties);
    }

    default MattockItem mattockItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties)
    {
        return new MattockItem(tier, attackDamage, attackSpeed, properties);
    }

    <T extends Recipe<?>> RecipeSerializer<T> recipeSerializer(RecipeSerializerImpl<T> impl);

    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BlockEntityFactory<T> factory, Supplier<? extends Block> block);

    <T extends AbstractContainerMenu> MenuType<T> containerType(ContainerFactory<T> factory);

    default ToolDamagingRecipe shapedToolDamagingRecipe(ResourceLocation id, Recipe<?> recipe, @Nullable Ingredient tool)
    {
        return new ToolDamagingRecipe.Shaped(id, recipe, tool);
    }

    default ToolDamagingRecipe shapelessToolDamagingRecipe(ResourceLocation id, Recipe<?> recipe, @Nullable Ingredient tool)
    {
        return new ToolDamagingRecipe.Shapeless(id, recipe, tool);
    }

    // APIs

    default void openScreen(ServerPlayer serverPlayer, MenuProvider provider, BlockPos pos)
    {
        openScreen(serverPlayer, provider, buffer -> buffer.writeBlockPos(pos));
    }

    void openScreen(ServerPlayer serverPlayer, MenuProvider provider, Consumer<FriendlyByteBuf> buffer);

    default ItemStack getCraftingRemainder(ItemStack stack)
    {
        final Item remainder = stack.getItem().getCraftingRemainingItem();
        if (stack.getItem().hasCraftingRemainingItem() && remainder != null)
        {
            return new ItemStack(remainder);
        }
        return ItemStack.EMPTY;
    }

    // Platform Properties

    boolean isDedicatedClient();
}
