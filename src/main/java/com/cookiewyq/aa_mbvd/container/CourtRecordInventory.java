package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class CourtRecordInventory implements Capability.IStorage<CourtRecordInventory>, IItemHandlerModifiable, ICapabilityProvider {
    private final ItemStackHandler handler;


    public CourtRecordInventory() {
        this.handler = new ItemStackHandler(ModConfigs.COURT_RECORD_ROWS.get() * 9);
    }

    public ItemStackHandler getHandler() {
        return handler;
    }

    /** 把当前数据写成 CompoundNBT（用于保存） */
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", handler.serializeNBT());
        return nbt;
    }

    /** 从 CompoundNBT 读回数据（用于加载） */
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt == null) handler.setSize(ModConfigs.COURT_RECORD_ROWS.get() * 9);
        if (nbt.contains("Items")) {
            handler.deserializeNBT(nbt.getCompound("Items"));
        }
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<CourtRecordInventory> capability, CourtRecordInventory courtRecordInventory, Direction direction) {
        return courtRecordInventory.serializeNBT();
    }

    @Override
    public void readNBT(Capability<CourtRecordInventory> capability, CourtRecordInventory courtRecordInventory, Direction direction, INBT inbt) {
        courtRecordInventory.deserializeNBT((CompoundNBT) inbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if (capability == Capabilities.COURT_RECORD_INVENTORY_CAPABILITY) {
            return LazyOptional.of(() -> this).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void setStackInSlot(int i, @Nonnull ItemStack itemStack) {
        this.handler.setStackInSlot(i, itemStack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int i) {
        return this.handler.getStackInSlot(i);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int i, @Nonnull ItemStack itemStack, boolean b) {
        return this.handler.insertItem(i, itemStack, b);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int i, int i1, boolean b) {
        return this.handler.extractItem(i, i1, b);
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public boolean isItemValid(int i, @Nonnull ItemStack itemStack) {
        return isEvidenceItem(itemStack);
    }

    private boolean isEvidenceItem(ItemStack stack) {
        // 获取物品的注册名
        ResourceLocation itemId = stack.getItem().getRegistryName();

        // 检查物品是否属于特定模组
        if (itemId != null) {
            // 允许您模组中的所有物品作为证据
            return itemId.getNamespace().equals("aa_mbvd");
        }

        return false;
    }
}