package com.alcatrazescapee.notreepunching.platform.forge;

import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.fml.loading.FMLLoader;

import com.alcatrazescapee.notreepunching.Config;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;

public final class ForgePlatform implements XPlatform
{
    @Override
    public <T> RegistryInterface<T> registryInterface(Registry<T> registry)
    {
        return new ForgeRegistryInterface<>(registry);
    }

    @Override
    public Config createConfig()
    {
        return ForgeConfig.create();
    }

    @Override
    public CreativeModeTab creativeTab(ResourceLocation id, Supplier<ItemStack> icon)
    {
        return new CreativeModeTab(id.toString())
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
    public boolean isDedicatedClient()
    {
        return FMLLoader.getDist() == Dist.CLIENT;
    }
}
