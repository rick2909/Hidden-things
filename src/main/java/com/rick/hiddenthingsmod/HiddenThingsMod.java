package com.rick.hiddenthingsmod;

import com.rick.hiddenthingsmod.blocks.HiddenChest;
import com.rick.hiddenthingsmod.lists.BlockList;
import com.rick.hiddenthingsmod.lists.ItemList;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.audio.Sound;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("hiddenthingsmod")
public class HiddenThingsMod {
    public static HiddenThingsMod instance;
    public static final String modid = "hiddenthingsmod";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger(modid);

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
        LOGGER.info("Setup registered");
    }

    private void clientRegisties(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("clientRegisties registered");
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegsitryEvents {
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event){
            event.getRegistry().registerAll(
                    ItemList.hidden_chest = new BlockItem(BlockList.hidden_chest, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName(BlockList.hidden_chest.getRegistryName())
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

        private static ResourceLocation location(String name) {
            return new ResourceLocation(modid, name);
        }
    }


}
