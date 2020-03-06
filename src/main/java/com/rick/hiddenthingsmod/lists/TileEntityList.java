package com.rick.hiddenthingsmod.lists;

import com.rick.hiddenthingsmod.blocks.HiddenChestTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityList {

    @ObjectHolder("hiddenthingsmod:hidden_chest")
    public static TileEntityType<HiddenChestTile> hidden_chest_tile;
}
