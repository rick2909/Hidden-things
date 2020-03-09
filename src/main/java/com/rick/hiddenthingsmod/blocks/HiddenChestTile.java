package com.rick.hiddenthingsmod.blocks;

import com.rick.hiddenthingsmod.Screens.Containers.HiddenChestContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

import static com.rick.hiddenthingsmod.lists.TileEntityList.hidden_chest_tile;

public class HiddenChestTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private ArrayList<Item> allowedItems = new ArrayList<>();

    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();

    private BlockState mimic;

    public HiddenChestTile() {
        super(hidden_chest_tile);
        allowedItems.add(Items.STONE);
        allowedItems.add(Items.COBBLESTONE);
        allowedItems.add(Items.ACACIA_WOOD);
        allowedItems.add(Items.BIRCH_WOOD);
        allowedItems.add(Items.DARK_OAK_WOOD);
        allowedItems.add(Items.JUNGLE_WOOD);
        allowedItems.add(Items.OAK_WOOD);
        allowedItems.add(Items.STRIPPED_ACACIA_WOOD);
        allowedItems.add(Items.STRIPPED_BIRCH_WOOD);
        allowedItems.add(Items.STRIPPED_SPRUCE_WOOD);
        allowedItems.add(Items.STRIPPED_JUNGLE_WOOD);
        allowedItems.add(Items.STRIPPED_DARK_OAK_WOOD);
        allowedItems.add(Items.STRIPPED_OAK_WOOD);
        allowedItems.add(Items.STRIPPED_DARK_OAK_LOG);
        allowedItems.add(Items.STRIPPED_ACACIA_LOG);
        allowedItems.add(Items.STRIPPED_JUNGLE_LOG);
        allowedItems.add(Items.STRIPPED_BIRCH_LOG);
        allowedItems.add(Items.STRIPPED_SPRUCE_LOG);
        allowedItems.add(Items.STRIPPED_OAK_LOG);
        allowedItems.add(Items.DARK_OAK_LOG);
        allowedItems.add(Items.ACACIA_LOG);
        allowedItems.add(Items.JUNGLE_LOG);
        allowedItems.add(Items.BIRCH_LOG);
        allowedItems.add(Items.SPRUCE_LOG);
        allowedItems.add(Items.OAK_LOG);
        allowedItems.add(Items.CHISELED_SANDSTONE);
        allowedItems.add(Items.SANDSTONE);
        allowedItems.add(Items.CUT_SANDSTONE);
        allowedItems.add(Items.LAPIS_BLOCK);
        allowedItems.add(Items.IRON_BLOCK);
        allowedItems.add(Items.GOLD_BLOCK);
        allowedItems.add(Items.BRICKS);
        allowedItems.add(Items.OBSIDIAN);
        allowedItems.add(Items.DIRT);
        allowedItems.add(Items.OAK_PLANKS);
        allowedItems.add(Items.SPRUCE_PLANKS);
        allowedItems.add(Items.BIRCH_PLANKS);
        allowedItems.add(Items.JUNGLE_PLANKS);
        allowedItems.add(Items.ACACIA_PLANKS);
        allowedItems.add(Items.DARK_OAK_PLANKS);
    }



    private boolean validBlock(ItemStack stack, boolean isInsert){
        if(isInsert && !stack.isEmpty() && allowedItems.contains(stack.getItem())) {//stack.getItem() instanceof BlockItem){
            BlockState mimicState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            this.setMinic(mimicState);
            return true;
        }else if(!isInsert && !stack.isEmpty() && allowedItems.contains(stack.getItem())) {//stack.getItem() instanceof BlockItem){
            return true;
        }

        return false;
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(28) {

            @Override
            public int getSlotLimit(int slot) {
                if(slot == 0){
                    return 1;
                }
                return super.getSlotLimit(slot);
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                System.out.println("isItemValid SLOT: " + slot + " STACK: "+stack);
                if (slot == 0 && validBlock(stack,true)) {
                    return validBlock(stack,false);
                }
                return true;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                System.out.println("is removed: "+stack +"so simulate is: " + simulate);
                if (slot == 0 && !validBlock(stack,true)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                setMinic(null);
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    public void setMinic(BlockState mimicState) {
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
    public void tick() {

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
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(invTag));
        super.read(tag);

        if (tag.contains("mimic")) {
            this.mimic = NBTUtil.readBlockState(tag.getCompound("mimic"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(h -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
            tag.put("inv", compound);
        });

        if (this.mimic != null) {
            tag.put("mimic", NBTUtil.writeBlockState(this.mimic));
        }

        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new HiddenChestContainer(i, world, pos, playerInventory, playerEntity);
    }

}
