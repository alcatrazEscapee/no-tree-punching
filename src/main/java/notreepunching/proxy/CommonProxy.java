package notreepunching.proxy;


import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import notreepunching.block.ModBlocks;
import notreepunching.event.HarvestEventHandler;
import notreepunching.event.PlayerEventHandler;
import notreepunching.item.ModItems;
import notreepunching.recipe.ModRecipes;

@Mod.EventBusSubscriber
public class CommonProxy{

    public void preInit(FMLPreInitializationEvent event){
        // Register Event Handlers
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    public void postInit(FMLPostInitializationEvent event){
        ModRecipes.init();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModItems.init();
        ModItems.registerItems(event);
        ModBlocks.registerItemBlocks(event);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModBlocks.init();
        ModBlocks.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ModRecipes.removeVanillaRecipes(event.getRegistry());
    }

    public void registerItemModel(Item item, int meta, String id) {
    }
    public void registerItemModel(Item item, int meta) {
    }
    public void registerItemModel(Item item){
    }

    public void registerItemModelWithVariant(Item item, int meta, String id, String variant){
    }

    public String localize(String unlocalized, Object... args) {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }

    public void generateParticle(World world, BlockPos pos, EnumParticleTypes particle){
    }

}


