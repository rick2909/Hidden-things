package com.rick.hiddenthingsmod.setup;

import com.rick.hiddenthingsmod.lists.BlockList;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModSetup {

    private static final String modid = "hiddenthingsmod";
    private static final Logger LOGGER = LogManager.getLogger(modid);

    public ItemGroup itemGroup = new ItemGroup("hidden_things") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(BlockList.hidden_chest);
        }
    };

    public void init(){
        LOGGER.info("Setup registered");

    }
}
