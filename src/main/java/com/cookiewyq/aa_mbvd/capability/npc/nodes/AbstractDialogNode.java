package com.cookiewyq.aa_mbvd.capability.npc.nodes;

import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public abstract class AbstractDialogNode implements INBTSerializable<INBT> {
    public abstract ITextComponent getRoleName();

    public abstract ITextComponent getContent();

    public abstract ITextComponent getReply();

    public abstract String getRoleNameKey();

    public abstract String getContentKey();

    public abstract String getReplyKey();

    public abstract void setRoleName(ITextComponent roleName);

    public abstract void setContent(ITextComponent content);

    public abstract void setReply(ITextComponent reply);

    public abstract void setRoleNameKey(String roleNameKey);

    public abstract void setContentKey(String contentKey);

    public abstract void setReplyKey(String replyKey);

    public abstract UUID getNextNodeId();

    public abstract void setNextNodeId(UUID nextNodeId);

    public abstract UUID getUUID();
}
