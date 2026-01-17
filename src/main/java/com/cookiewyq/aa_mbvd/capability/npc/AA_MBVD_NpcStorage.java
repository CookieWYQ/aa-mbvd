package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
public class AA_MBVD_NpcStorage implements Capability.IStorage<IAA_MBVD_NpcCapability> {

    @Override
    public CompoundNBT writeNBT(Capability<IAA_MBVD_NpcCapability> capability, IAA_MBVD_NpcCapability instance, Direction side) {
        if(instance instanceof AA_MBVD_NpcCapability) {
            return ((AA_MBVD_NpcCapability)instance).serializeNBT();
        }
        return new CompoundNBT();
    }

    @Override
    public void readNBT(Capability<IAA_MBVD_NpcCapability> capability, IAA_MBVD_NpcCapability instance, Direction side, INBT nbt) {
        if(instance instanceof AA_MBVD_NpcCapability) {
            instance.deserializeNBT(nbt);
        }
    }
}
