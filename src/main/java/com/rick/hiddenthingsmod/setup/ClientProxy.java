package com.rick.hiddenthingsmod.setup;

import com.rick.hiddenthingsmod.Screens.HiddenChestScreen;
import com.rick.hiddenthingsmod.lists.BlockList;
import com.rick.hiddenthingsmod.lists.ContainerList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ScreenManager.registerFactory(ContainerList.hidden_chest_container, HiddenChestScreen::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
