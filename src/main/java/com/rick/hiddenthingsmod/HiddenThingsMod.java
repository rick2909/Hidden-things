package com.rick.hiddenthingsmod;

import com.rick.hiddenthingsmod.Screens.Containers.HiddenChestContainer;
import com.rick.hiddenthingsmod.blocks.*;
import com.rick.hiddenthingsmod.items.SecretKey;
import com.rick.hiddenthingsmod.lists.BlockList;
import com.rick.hiddenthingsmod.lists.ItemList;
import com.rick.hiddenthingsmod.setup.ClientProxy;
import com.rick.hiddenthingsmod.setup.IProxy;
import com.rick.hiddenthingsmod.setup.ModSetup;
import com.rick.hiddenthingsmod.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("hiddenthingsmod")
public class HiddenThingsMod {
    public static HiddenThingsMod instance;
    public static final String MODID = "hiddenthingsmod";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
    public static ModSetup setup = new ModSetup();

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public HiddenThingsMod() {
        instance = this;

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientRegisties);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        setup.init();
        proxy.init();
    }

    private void clientRegisties(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("clientRegisties registered");
        ModelLoaderRegistry.registerLoader(new ResourceLocation(MODID, "hiddenchestloader"), new HiddenChestModelLoader());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegsitryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event){
            Item.Properties group = new Item.Properties().group(setup.itemGroup);
            event.getRegistry().registerAll(
                    //items
                    ItemList.secret_key = new SecretKey(),
                    //blocks
                    ItemList.hidden_chest = new BlockItem(BlockList.hidden_chest, group).setRegistryName(BlockList.hidden_chest.getRegistryName())
            );
            LOGGER.info("Items registered");
        }

        @SubscribeEvent
        public static void registerBlocks(final RegistryEvent.Register<Block> event){
            event.getRegistry().registerAll(
                    BlockList.hidden_chest = new HiddenChest()

            );

            LOGGER.info("Blocks registered");
        }

        @SubscribeEvent
        public static void registerTileEntity(final RegistryEvent.Register<TileEntityType<?>> event){
            event.getRegistry().register(TileEntityType.Builder.create(HiddenChestTile::new, BlockList.hidden_chest).build(null).setRegistryName(BlockList.hidden_chest.getRegistryName()));

            LOGGER.info("Tile Entitys registered");
        }

        @SubscribeEvent
        public static void registerContainer(final RegistryEvent.Register<ContainerType<?>> event){
            event.getRegistry().register(IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                return new HiddenChestContainer(windowId, HiddenThingsMod.proxy.getClientWorld(), pos, inv, HiddenThingsMod.proxy.getClientPlayer());
            })).setRegistryName(BlockList.hidden_chest.getRegistryName()));
        }
    }
}
