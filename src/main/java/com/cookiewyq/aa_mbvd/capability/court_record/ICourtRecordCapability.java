package com.cookiewyq.aa_mbvd.capability.court_record;

import net.minecraftforge.items.ItemStackHandler;

public interface ICourtRecordCapability {
    ItemStackHandler getInventory();
    boolean isAttorneysBadge();
    void setIsAttorneysBadge(boolean isAttorneysBadge);
}