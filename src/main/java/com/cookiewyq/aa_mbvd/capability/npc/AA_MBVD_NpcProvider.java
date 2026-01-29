package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

/**
 * NPC 能力提供者
 */
public class AA_MBVD_NpcProvider implements ICapabilitySerializable<CompoundNBT> {

    private final AA_MBVD_NpcCapability instance = new AA_MBVD_NpcCapability();
    private final LazyOptional<AA_MBVD_NpcCapability> optional =
            LazyOptional.of(() -> instance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == Capabilities.AA_MBVD_NPC_CAPABILITY ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) Capabilities.AA_MBVD_NPC_CAPABILITY.getStorage().writeNBT(Capabilities.AA_MBVD_NPC_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        Capabilities.AA_MBVD_NPC_CAPABILITY.getStorage().readNBT(Capabilities.AA_MBVD_NPC_CAPABILITY, instance, null, nbt);
    }
}