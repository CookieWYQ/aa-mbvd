package com.cookiewyq.aa_mbvd.capability.showing_evidence;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.events.HudClientEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IShowingEvidenceData extends ICapabilitySerializable<CompoundNBT> {
    HudClientEvent.STATE getState();

    boolean isShowEvidence();

    ItemStack getDisplayedItem();

    @Nullable
    PlayerEntity getDisplayingEvidencePlayer();

    long getDisplayingItemTime();

    void setState(HudClientEvent.STATE state);

    void setShowEvidence(boolean showEvidence);

    void setDisplayedItem(ItemStack itemStack);

    void setDisplayingEvidencePlayer(PlayerEntity player);

    void setDisplayingItemTime(long time);


    // 添加 Provider 内部类
    class Provider implements ICapabilityProvider, ICapabilitySerializable<CompoundNBT> {
        private final IShowingEvidenceData showingEvidenceData = new ShowingEvidenceData();
        private final LazyOptional<IShowingEvidenceData> opt = LazyOptional.of(() -> showingEvidenceData);

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
            CompoundNBT nbt = showingEvidenceData.serializeNBT();
            return nbt != null ? nbt : new CompoundNBT();
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            if (nbt != null) {
                showingEvidenceData.deserializeNBT(nbt);
            }
        }
    }
}
