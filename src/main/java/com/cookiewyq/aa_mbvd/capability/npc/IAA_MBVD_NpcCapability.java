package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IAA_MBVD_NpcCapability extends INBTSerializable<INBT> {
    boolean isNpc();

    void setNpc(boolean npc);

    List<AbstractDialogNode> getDialogNodes();

    void clearDialogNodes();

    void addDialogNode(AbstractDialogNode node);

    void removeDialogNode(AbstractDialogNode node);

    @Nullable
    AbstractDialogNode getCurrentNode();

    void setCurrentNode(AbstractDialogNode node);

    AbstractDialogNode getDialogNodeByID(String uuid);
}
