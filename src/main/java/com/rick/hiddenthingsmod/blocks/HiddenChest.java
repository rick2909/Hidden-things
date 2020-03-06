package com.rick.hiddenthingsmod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class HiddenChest extends Block {
    public HiddenChest(){
        super(Properties.create(Material.WOOD)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f, 3.0f)
        );
        setRegistryName("hidden_chest");
    }
}
