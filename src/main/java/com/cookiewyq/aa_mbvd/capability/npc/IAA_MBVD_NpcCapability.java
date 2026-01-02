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

    ITextComponent getNpcName();

    void setNpcName(ITextComponent npcName);

    List<AbstractDialogNode> getDialogNodes();

    void clearDialogNodes();

    void setDialogNodeMap(Map<UUID, AbstractDialogNode> dialogMap);

    void addDialogNode(AbstractDialogNode node);

    @Nullable
    AbstractDialogNode getDialogNode(UUID uuid);

    @Nullable
    AbstractDialogNode getCurrentNode();
}
