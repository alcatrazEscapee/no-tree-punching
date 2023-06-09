package com.alcatrazescapee.notreepunching.common.items;

import java.util.ArrayList;
import java.util.List;
import com.alcatrazescapee.notreepunching.NoTreePunching;
import com.alcatrazescapee.notreepunching.platform.RegistryHolder;
import com.alcatrazescapee.notreepunching.platform.RegistryInterface;
import com.alcatrazescapee.notreepunching.platform.XPlatform;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
public final class ModItemGroups
{
    public static final RegistryInterface<CreativeModeTab> TABS = XPlatform.INSTANCE.registryInterface(BuiltInRegistries.CREATIVE_MODE_TAB);
    public static final List<RegistryHolder<? extends ItemLike>> ENTRIES = new ArrayList<>();

    public static final RegistryHolder<CreativeModeTab> TAB = TABS.register("items", () -> XPlatform.INSTANCE.creativeTab()
        .icon(() -> new ItemStack(ModItems.FLINT_PICKAXE.get()))
        .title(Component.translatable(NoTreePunching.MOD_ID + ".items"))
        .displayItems((params, output) -> {
            for (RegistryHolder<? extends ItemLike> entry : ENTRIES)
            {
                output.accept(entry.get());
            }
        })
        .build());
}
