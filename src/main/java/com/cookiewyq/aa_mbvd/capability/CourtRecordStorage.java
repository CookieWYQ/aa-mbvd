package com.cookiewyq.aa_mbvd.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CourtRecordStorage implements Capability.IStorage<ICourtRecordCapability> {

    @Override
    public INBT writeNBT(Capability<ICourtRecordCapability> cap, ICourtRecordCapability inst, Direction side) {
        return inst.getInventory().serializeNBT();
    }

    @Override
    public void readNBT(Capability<ICourtRecordCapability> cap, ICourtRecordCapability inst, Direction side, INBT nbt) {
        inst.getInventory().deserializeNBT((CompoundNBT) nbt);
    }
}
