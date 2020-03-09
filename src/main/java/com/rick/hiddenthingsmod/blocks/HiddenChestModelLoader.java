package com.rick.hiddenthingsmod.blocks;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.client.model.IModelLoader;

public class HiddenChestModelLoader implements IModelLoader<HiddenChestModelGeometry> {

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }

    @Override
    public HiddenChestModelGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        return new HiddenChestModelGeometry();
    }
}
