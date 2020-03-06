package com.rick.hiddenthingsmod.lists;

import com.rick.hiddenthingsmod.Screens.Containers.HiddenChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerList {
    @ObjectHolder("hiddenthingsmod:hidden_chest")
    public static ContainerType<HiddenChestContainer> hidden_chest_container;
}
