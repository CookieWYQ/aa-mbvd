package com.cookiewyq.aa_mbvd.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CourtRecordStorage implements Capability.IStorage<ICourtRecordCapability> {

    @Override
    public INBT writeNBT(Capability<ICourtRecordCapability> cap, ICourtRecordCapability inst, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("isAttorneysBadge", inst.isAttorneysBadge());
        nbt.put("inventory", inst.getInventory().serializeNBT());
        return nbt;
    }

    @Override
    public void readNBT(Capability<ICourtRecordCapability> cap, ICourtRecordCapability inst, Direction side, INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        inst.setIsAttorneysBadge(compoundNBT.getBoolean("isAttorneysBadge"));
        inst.getInventory().deserializeNBT(compoundNBT.getCompound("inventory"));
    }
}
