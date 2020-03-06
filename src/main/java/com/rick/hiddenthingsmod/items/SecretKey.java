package com.rick.hiddenthingsmod.items;

import com.rick.hiddenthingsmod.HiddenThingsMod;
import net.minecraft.item.Item;

public class SecretKey extends Item {

    public SecretKey() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(HiddenThingsMod.setup.itemGroup)
        );

        setRegistryName("secret_key");
    }
}
