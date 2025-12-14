package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CourtRecordCapability implements ICourtRecordCapability {
    // 法庭记录槽位：3行7列 = 21个槽位
    private static final int COURT_RECORD_SLOTS = 21;
    // 玩家槽位：快捷栏9个 + 背包27个 = 36个槽位
    private static final int PLAYER_SLOTS = 36;

    private final ItemStack[] courtRecordSlots = new ItemStack[COURT_RECORD_SLOTS];
    private final ItemStack[] playerInventory = new ItemStack[PLAYER_SLOTS];
    private boolean dirty = false;

    // 新增字段：记录当前佩戴的饰品类型
    private boolean isAttorneysBadge = true;

    private final ItemStackHandler inventory;

    public CourtRecordCapability() {
        inventory = new ItemStackHandler(ModConfigs.COURT_RECORD_ROWS.get() * 9) {

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem().getRegistryName() != null &&
                        "aa_mbvd".equals(stack.getItem().getRegistryName().getNamespace());
            }
        };
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

    @Override
    public boolean isAttorneysBadge() {
        return isAttorneysBadge;
    }

    public void setIsAttorneysBadge(boolean isAttorneysBadge) {
        this.isAttorneysBadge = isAttorneysBadge;
    }
}