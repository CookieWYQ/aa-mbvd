package com.cookiewyq.aa_mbvd.capability.npc.nodes;

import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractDialogNode implements INBTSerializable<INBT> {
    public abstract ITextComponent getRoleName();

    public abstract ITextComponent getContent();

    public abstract void setRoleName(ITextComponent roleName);

    public abstract void setContent(ITextComponent content);

    public abstract String getNextNodeId();

    public abstract void setNextNodeId(String nextNodeId);

    public abstract String getID();

    public abstract void setID(String id);
}
