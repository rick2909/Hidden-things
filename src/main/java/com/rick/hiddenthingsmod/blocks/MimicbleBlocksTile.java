package com.rick.hiddenthingsmod.blocks;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.util.Constants;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

public class MimicbleBlocksTile extends TileEntity {

    protected static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    protected BlockState mimic;

    protected MimicbleBlocksTile(TileEntityType<?> tileEntityTypeIn){
        super(tileEntityTypeIn);
        init();
    }

    protected ArrayList<Item> bannedItems = new ArrayList<>();

    private void init() {
        bannedItems.add(Items.CHEST);
        bannedItems.add(Items.GRASS);
        bannedItems.add(Items.GRASS_BLOCK);
        bannedItems.add(Items.GRASS_PATH);
        bannedItems.add(Items.REDSTONE);
        bannedItems.add(Items.LECTERN);
        bannedItems.add(Items.HOPPER);
        bannedItems.add(Items.DAYLIGHT_DETECTOR);
        bannedItems.add(Items.ENCHANTING_TABLE);
        bannedItems.add(Items.FLETCHING_TABLE);
        bannedItems.add(Items.SMITHING_TABLE);
        bannedItems.add(Items.CARTOGRAPHY_TABLE);
        bannedItems.add(Items.REPEATER);
        bannedItems.add(Items.COMPARATOR);
    }

    private boolean bannedBlocksToMimic(Block block){
        // PressurePlateBlock WeightedPressurePlateBlock TrapDoorBlock LecternBlock FenceGateBlock
        return     !(block instanceof SlabBlock)
                && !(block instanceof StairsBlock)
                && !(block instanceof FenceBlock)
                && !(block instanceof CraftingTableBlock)
                && !(block instanceof CartographyTableBlock)
                && !(block instanceof CarpetBlock)
                && !(block instanceof EnchantingTableBlock)
                && !(block instanceof WoodButtonBlock)
                && !(block instanceof StoneButtonBlock)
                && !(block instanceof WallBlock);
    }

    protected boolean validBlock(ItemStack stack, boolean isInsert){
        if(isInsert && !stack.isEmpty() && stack.getItem() instanceof BlockItem){
            Block block = ((BlockItem) stack.getItem()).getBlock();
            BlockState mimicState = block.getDefaultState();
            System.out.println(((BlockItem) stack.getItem()).getBlock());
            if(block.isSolid(mimicState) && bannedBlocksToMimic(block) && !bannedItems.contains(stack.getItem())){
                this.setMinic(mimicState);
                return true;
            }
        }else if(!isInsert && !stack.isEmpty() && stack.getItem() instanceof BlockItem){
            return true;
        }

        return false;
    }

    protected void setMinic(BlockState mimicState) {
        this.mimic = mimicState;
        this.markDirty();
        world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);

        // TODO GO VIA onDataPacket()
        if(world.isRemote){
            ModelDataManager.requestModelDataRefresh(this);
        }
        System.out.println("MIMIC: " + mimic);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        if (this.mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(this.mimic));
        }
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        BlockState oldMimic = this.mimic;
        CompoundNBT tag = pkt.getNbtCompound();
        if (tag.contains("mimic")) {
            this.mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
            if (!Objects.equals(oldMimic, this.mimic)) {
                ModelDataManager.requestModelDataRefresh(this);
                world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
                System.out.println("MIMIC: UPDATED TO NEW BLOCK!");
            }
        }
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        return new ModelDataMap.Builder()
                .withInitial(MIMIC, this.mimic)
                .build();
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        if (tag.contains("mimic")) {
            this.mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if (this.mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(this.mimic));
        }

        return super.write(tag);
    }

}
