package com.cookiewyq.aa_mbvd.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import javax.annotation.Nonnull;

public class CourtRecordProvider implements ICapabilitySerializable<INBT> {

    private final ICourtRecordCapability instance = new CourtRecordCapability();
    private final LazyOptional<ICourtRecordCapability> optional = LazyOptional.of(() -> instance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return Capabilities.COURT_RECORD_CAPABILITY.orEmpty(cap, optional);
    }

    @Override
    public INBT serializeNBT() {
        return Capabilities.COURT_RECORD_CAPABILITY.getStorage().writeNBT(Capabilities.COURT_RECORD_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        Capabilities.COURT_RECORD_CAPABILITY.getStorage().readNBT(Capabilities.COURT_RECORD_CAPABILITY, instance, null, nbt);
    }
}
