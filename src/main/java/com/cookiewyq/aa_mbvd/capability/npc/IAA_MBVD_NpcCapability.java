package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

public interface IAA_MBVD_NpcCapability extends INBTSerializable<CompoundNBT> {
    boolean isNpc();

    void setNpc(boolean npc);

    List<AbstractDialogNode> getDialogNodes();

    void clearDialogNodes();

    boolean addDialogNode(AbstractDialogNode node);

    void removeDialogNode(AbstractDialogNode node);

    boolean isDialogNodeExists(String nodeId);

    @Nullable
    AbstractDialogNode getCurrentNode();

    void setCurrentNode(AbstractDialogNode node);

    AbstractDialogNode getDialogNodeByID(String uuid);
}
