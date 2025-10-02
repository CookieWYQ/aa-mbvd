package com.cookiewyq.aa_mbvd.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IGetPlayerCourtRecordsTileEntityData {
    ItemStack[] getCourtRecordsItems();
    void setCourtRecordsItems(ItemStack[] items);
    CompoundNBT serializeNBT();
    void deserializeNBT(CompoundNBT nbt);

    class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
        private final IGetPlayerCourtRecordsTileEntityData iGetPlayerCourtRecordsTileEntity = new getPlayerCourtRecordsTileEntityData();
        private final LazyOptional<IGetPlayerCourtRecordsTileEntityData> opt = LazyOptional.of(() -> iGetPlayerCourtRecordsTileEntity);
        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY) {
                return opt.cast();
            }
            return LazyOptional.empty();
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
            return getCapability(cap, null);
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT nbt = iGetPlayerCourtRecordsTileEntity.serializeNBT();
            return nbt != null ? nbt : new CompoundNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if (nbt != null) {
                iGetPlayerCourtRecordsTileEntity.deserializeNBT(nbt);
            }
        }
    }
}
