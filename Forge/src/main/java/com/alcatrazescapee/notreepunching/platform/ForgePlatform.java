package com.alcatrazescapee.notreepunching.platform;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.common.items.CeramicBucketItem;
import com.alcatrazescapee.notreepunching.common.items.ForgeBucketItem;
import com.alcatrazescapee.notreepunching.common.items.ForgeMattockItem;
import com.alcatrazescapee.notreepunching.common.items.MattockItem;
import com.alcatrazescapee.notreepunching.common.recipes.ForgeShapedToolDamagingRecipe;
import com.alcatrazescapee.notreepunching.common.recipes.RecipeSerializerImpl;
import com.alcatrazescapee.notreepunching.common.recipes.ToolDamagingRecipe;
import com.alcatrazescapee.notreepunching.platform.event.BlockEntityFactory;
import com.alcatrazescapee.notreepunching.platform.event.ContainerFactory;

public final class ForgePlatform implements XPlatform
{
    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new ForgeRegistryInterface<>(registry);
    }

    @Override
    public CreativeModeTab creativeTab(ResourceLocation id, Supplier<ItemStack> icon)
    {
        return new CreativeModeTab(id.getNamespace() + "." + id.getPath())
        {
            @Override
            public ItemStack makeIcon()
            {
                return icon.get();
            }
        };
    }

    @Override
    public Tier toolTier(int uses, float speed, float attackDamageBonus, int level, int enchantmentValue, TagKey<Block> tag, Supplier<Ingredient> repairIngredient)
    {
        return new ForgeTier(level, uses, speed, attackDamageBonus, enchantmentValue, tag, repairIngredient);
    }

    @Override
    public StairBlock stairBlock(Supplier<BlockState> state, BlockBehaviour.Properties properties)
    {
        return new StairBlock(state, properties);
    }

    @Override
    public CeramicBucketItem bucketItem(Fluid fluid, Item.Properties properties)
    {
        return new ForgeBucketItem(fluid, properties);
    }

    @Override
    public MattockItem mattockItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties properties)
    {
        return new ForgeMattockItem(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public <T extends Recipe<?>> RecipeSerializer<T> recipeSerializer(RecipeSerializerImpl<T> impl)
    {
        return new ForgeRecipeSerializer<>(impl);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BlockEntityFactory<T> factory, Supplier<? extends Block> block)
    {
        return BlockEntityType.Builder.of(factory::create, block.get()).build(null);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> containerType(ContainerFactory<T> factory)
    {
        return IForgeMenuType.create(factory::create);
    }

    @Override
    public ToolDamagingRecipe shapedToolDamagingRecipe(ResourceLocation id, Recipe<?> recipe, @Nullable Ingredient tool)
    {
        return new ForgeShapedToolDamagingRecipe(id, recipe, tool);
    }

    @Override
    public void openScreen(ServerPlayer serverPlayer, MenuProvider provider, Consumer<FriendlyByteBuf> buffer)
    {
        NetworkHooks.openGui(serverPlayer, provider, buffer);
    }

    @Override
    public ItemStack getCraftingRemainder(ItemStack stack)
    {
        if (stack.hasContainerItem())
        {
            return stack.getContainerItem();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isUsingCorrectTier(BlockState state, Tier tier)
    {
        return TierSortingRegistry.isCorrectTierForDrops(tier, state);
    }

    @Override
    public boolean isDedicatedClient()
    {
        return FMLLoader.getDist() == Dist.CLIENT;
    }

    @Override
    public Path configPath()
    {
        return FMLPaths.CONFIGDIR.get();
    }
}
