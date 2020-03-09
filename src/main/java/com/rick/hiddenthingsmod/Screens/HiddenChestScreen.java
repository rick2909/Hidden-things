package com.rick.hiddenthingsmod.Screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rick.hiddenthingsmod.HiddenThingsMod;
import com.rick.hiddenthingsmod.Screens.Containers.HiddenChestContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class HiddenChestScreen extends ContainerScreen<HiddenChestContainer> {

    private ResourceLocation GUI = new ResourceLocation(HiddenThingsMod.MODID, "textures/gui/hidden_chest_gui.png");

    private int newXSize = this.xSize;
    private int newYSize = this.ySize+38;

    public HiddenChestScreen(HiddenChestContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.newXSize) / 2;
        int relY = (this.height - this.newYSize) / 2;
        this.blit(relX, relY, 0, 0, this.newXSize, this.newYSize);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.font.drawString("Hidden Chest", 8.0F, -9.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.newYSize - 113 + 2), 4210752);
    }


}
