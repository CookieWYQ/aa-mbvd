package com.cookiewyq.aa_mbvd.capability.npc.nodes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CommonDialogNode extends AbstractDialogNode{

    private ITextComponent roleName = new TranslationTextComponent("npc.aa_mbvd.default.role_name");
    private ITextComponent content = new TranslationTextComponent("npc.aa_mbvd.default.content");
    private String nodeID = "default";
    private String nextNodeId = "default";

    @Override
    public ITextComponent getRoleName() {
        return roleName;
    }

    @Override
    public ITextComponent getContent() {
        return content;
    }

    @Override
    public void setRoleName(ITextComponent roleName) {
        this.roleName = roleName;
    }

    @Override
    public void setContent(ITextComponent content) {
        this.content = content;
    }

    @Override
    public String getNextNodeId() {
        return nextNodeId;
    }

    @Override
    public void setNextNodeId(String nextNodeId) {
        this.nextNodeId = nextNodeId;
    }


    @Override
    public String getID() {
        return nodeID;
    }

    @Override
    public void setID(String id) {
        this.nodeID = id;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("nodeID", nodeID);
        nbt.putString("nextNodeId", nextNodeId);
        nbt.putString("roleName", ITextComponent.Serializer.toJson(roleName));
        nbt.putString("content", ITextComponent.Serializer.toJson(content));
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT inbt) {
        CompoundNBT nbt = (CompoundNBT) inbt;
        nodeID = nbt.getString("nodeID");
        nextNodeId = nbt.getString("nextNodeId");
        roleName = ITextComponent.Serializer.getComponentFromJson(nbt.getString("roleName"));
        content = ITextComponent.Serializer.getComponentFromJson(nbt.getString("content"));
    }
}
