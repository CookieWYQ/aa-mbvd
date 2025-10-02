package com.cookiewyq.aa_mbvd.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.concurrent.Callable;

public class getPlayerCourtRecordsTileEntityData implements IGetPlayerCourtRecordsTileEntityData {
    private ItemStack[] courtRecordsItems = new ItemStack[54];

    public getPlayerCourtRecordsTileEntityData() {
        // 初始化数组
        for (int i = 0; i < 54; i++) {
            courtRecordsItems[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack[] getCourtRecordsItems() {
        return courtRecordsItems;
    }

    @Override
    public void setCourtRecordsItems(ItemStack[] items) {
        this.courtRecordsItems = items;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        // 序列化CourtRecords物品
        CompoundNBT courtRecordsNBT = new CompoundNBT();
        for (int i = 0; i < courtRecordsItems.length; i++) {
            if (!courtRecordsItems[i].isEmpty()) {
                courtRecordsNBT.put("item_" + i, courtRecordsItems[i].write(new CompoundNBT()));
            }
        }
        compound.put("CourtRecordsItems", courtRecordsNBT);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.contains("CourtRecordsItems")) {
            CompoundNBT courtRecordsNBT = nbt.getCompound("CourtRecordsItems");
            for (int i = 0; i < 54; i++) {
                if (courtRecordsNBT.contains("item_" + i)) {
                    courtRecordsItems[i] = ItemStack.read(courtRecordsNBT.getCompound("item_" + i));
                } else {
                    courtRecordsItems[i] = ItemStack.EMPTY;
                }
            }
        }
    }

    public static class Storage implements Capability.IStorage<IGetPlayerCourtRecordsTileEntityData> {
        @Override
        public CompoundNBT writeNBT(Capability<IGetPlayerCourtRecordsTileEntityData> capability, IGetPlayerCourtRecordsTileEntityData instance, Direction side) {
            return instance.serializeNBT();
        }

        @Override
        public void readNBT(Capability<IGetPlayerCourtRecordsTileEntityData> capability, IGetPlayerCourtRecordsTileEntityData instance, Direction direction, INBT inbt) {
            instance.deserializeNBT((CompoundNBT) inbt);
        }
    }

    public static class Factory implements Callable<IGetPlayerCourtRecordsTileEntityData> {
        @Override
        public IGetPlayerCourtRecordsTileEntityData call() throws Exception {
            return new getPlayerCourtRecordsTileEntityData();
        }
    }
}
