package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.events.HudClientEvent;
import com.cookiewyq.aa_mbvd.events.ModTickEvents;
import com.cookiewyq.aa_mbvd.util.tools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ShowingEvidenceData implements IShowingEvidenceData {

    private HudClientEvent.STATE state = HudClientEvent.STATE.None;
    private boolean isShowEvidence = false;
    private ItemStack displayedItem = ItemStack.EMPTY;
    private PlayerEntity displayingEvidencePlayer = null;
    private long displayingItemTime = 0;

    public ShowingEvidenceData() {

    }

    @Override
    public HudClientEvent.STATE getState() {
        return state;
    }

    @Override
    public boolean isShowEvidence() {
        return isShowEvidence;
    }

    @Override
    public ItemStack getDisplayedItem() {
        return displayedItem;
    }

    @Override
    public PlayerEntity getDisplayingEvidencePlayer() {
        return displayingEvidencePlayer;
    }

    @Override
    public long getDisplayingItemTime() {
        return displayingItemTime;
    }

    @Override
    public void setState(HudClientEvent.STATE state) {
        this.state = state != null ? state : HudClientEvent.STATE.None;
    }

    @Override
    public void setShowEvidence(boolean showEvidence) {
        this.isShowEvidence = showEvidence;
    }

    @Override
    public void setDisplayedItem(ItemStack itemStack) {
        this.displayedItem = itemStack;
    }

    @Override
    public void setDisplayingEvidencePlayer(PlayerEntity player) {
        this.displayingEvidencePlayer = player;
    }

    @Override
    public void setDisplayingItemTime(long time) {
        this.displayingItemTime = time;
    }

    @Override
    @Nullable
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("state", this.state != null ? this.state.name() : HudClientEvent.STATE.None.name());
        nbt.putBoolean("showEvidence", this.isShowEvidence);

        // 只在玩家存在时序列化这些数据
        if (this.displayingEvidencePlayer != null) {
            nbt.put("displayedItem", this.displayedItem.serializeNBT());
            nbt.putLong("displayingItemTime", this.displayingItemTime);
            nbt.putUniqueId("displayingEvidencePlayer", this.displayingEvidencePlayer.getUniqueID());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT compoundNBT) {
        if (compoundNBT != null) {
            try {
                this.state = HudClientEvent.STATE.valueOf(compoundNBT.getString("state"));
            } catch (Exception e) {
                this.state = HudClientEvent.STATE.None;
            }
            this.isShowEvidence = compoundNBT.getBoolean("showEvidence");

            // 添加对 displayedItem 的处理
            if (compoundNBT.contains("displayedItem")) {
                this.displayedItem = ItemStack.read(compoundNBT.getCompound("displayedItem"));
            }

            // 添加对 displayingItemTime 的处理
            if (compoundNBT.contains("displayingItemTime")) {
                this.displayingItemTime = compoundNBT.getLong("displayingItemTime");
            }

            // 添加对 displayingEvidencePlayer UUID 的空值检查
            if (compoundNBT.hasUniqueId("displayingEvidencePlayer")) {
                this.displayingEvidencePlayer = tools.getPlayerEntityFromUUIDByTickEvent(compoundNBT.getUniqueId("displayingEvidencePlayer"));
            }
        } else {
            this.state = HudClientEvent.STATE.None;
            this.isShowEvidence = false;
        }
    }


    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        LazyOptional<IShowingEvidenceData> opt = LazyOptional.of(() -> this);
        if (capability == Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    // 添加 Storage 内部类实现 NBT 序列化
    public static class Storage implements IStorage<IShowingEvidenceData> {
        @Nullable
        @Override
        public INBT writeNBT(Capability<IShowingEvidenceData> capability, IShowingEvidenceData instance, Direction side) {
            if (instance instanceof ShowingEvidenceData) {
                return instance.serializeNBT();
            }
            return new CompoundNBT();
        }

        @Override
        public void readNBT(Capability<IShowingEvidenceData> capability, IShowingEvidenceData instance, Direction side, INBT nbt) {
            if (instance instanceof ShowingEvidenceData && nbt instanceof CompoundNBT) {
                instance.deserializeNBT((CompoundNBT) nbt);
            }
        }
    }
}
