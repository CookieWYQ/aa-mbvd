package com.cookiewyq.aa_mbvd.capability.npc.nodes;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class CommonDialogNode extends AbstractDialogNode{

    private ITextComponent roleName = new TranslationTextComponent("npc.aa_mbvd.default.role_name");
    private ITextComponent content = new TranslationTextComponent("npc.aa_mbvd.default.content");
    private ITextComponent reply = new TranslationTextComponent("npc.aa_mbvd.default.reply");
    private String roleNameKey = "npc.aa_mbvd.default.role_name";
    private String contentKey = "npc.aa_mbvd.default.content";
    private String replyKey = "npc.aa_mbvd.default.reply";
    private UUID nextNodeId = null;
    private final UUID uuid = UUID.randomUUID();

    @Override
    public ITextComponent getRoleName() {
        return roleName;
    }

    @Override
    public ITextComponent getContent() {
        return content;
    }

    @Override
    public ITextComponent getReply() {
        return reply;
    }

    @Override
    public String getRoleNameKey() {
        return roleNameKey;
    }

    @Override
    public String getContentKey() {
        return contentKey;
    }

    @Override
    public String getReplyKey() {
        return replyKey;
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
    public void setReply(ITextComponent reply) {
        this.reply = reply;
    }

    @Override
    public void setRoleNameKey(String roleNameKey) {
        this.roleNameKey = roleNameKey;
    }

    @Override
    public void setContentKey(String contentKey) {
        this.contentKey = contentKey;
    }

    @Override
    public void setReplyKey(String replyKey) {
        this.replyKey = replyKey;
    }

    @Override
    public UUID getNextNodeId() {
        return nextNodeId;
    }

    @Override
    public void setNextNodeId(UUID nextNodeId) {
        this.nextNodeId = nextNodeId;
    }


    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public INBT serializeNBT() {
        CompoundNBT nbt_info = new CompoundNBT();
//        nbt_info.putString("roleName", roleNameKey);
//        nbt_info.putString("content", contentKey);
//        nbt_info.putString("reply", replyKey);
        nbt_info.putString("roleName", ITextComponent.Serializer.toJson(roleName));
        nbt_info.putString("content", ITextComponent.Serializer.toJson(content));
        nbt_info.putString("reply", ITextComponent.Serializer.toJson(reply));
        CompoundNBT nbt_f = new CompoundNBT();
        nbt_f.put(uuid.toString(), nbt_info);
        return nbt_f;
    }

    @Override
    public void deserializeNBT(INBT inbt) {
        CompoundNBT nbt = (CompoundNBT) inbt;
        CompoundNBT nbt_info = nbt.getCompound(uuid.toString());
//        roleNameKey = nbt_info.getString("roleName");
//        contentKey = nbt_info.getString("content");
//        replyKey = nbt_info.getString("reply");
//        roleName = new TranslationTextComponent(roleNameKey);
//        content = new TranslationTextComponent(contentKey);
//        reply = new TranslationTextComponent(replyKey);
        roleName = ITextComponent.Serializer.getComponentFromJson(nbt_info.getString("roleName"));
        content = ITextComponent.Serializer.getComponentFromJson(nbt_info.getString("content"));
        reply = ITextComponent.Serializer.getComponentFromJson(nbt_info.getString("reply"));
    }
}
