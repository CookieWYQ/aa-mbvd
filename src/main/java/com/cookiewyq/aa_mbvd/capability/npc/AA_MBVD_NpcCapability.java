package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import javax.annotation.Nullable;
import java.util.*;

/**
 * NPC 能力
 * 挂载在 LivingEntity 上
 */
public class AA_MBVD_NpcCapability implements IAA_MBVD_NpcCapability {

    /** 是否为 NPC */
    private boolean isNpc = false;

    private final ArrayList<AbstractDialogNode> dialogNodes = new ArrayList<AbstractDialogNode>() {{
        add(defaultDialogNode);
    }};


    private String currentNodeID;

    public static final CommonDialogNode defaultDialogNode = new CommonDialogNode();

    public boolean isNpc() {
        return isNpc;
    }

    public void setNpc(boolean npc) {
        this.isNpc = npc;
    }

    @Override
    public ArrayList<AbstractDialogNode> getDialogNodes() {
        return this.dialogNodes;
    }

    @Override
    public void clearDialogNodes() {
        this.dialogNodes.clear();
    }

    @Override
    public void addDialogNode(AbstractDialogNode node) {
        this.dialogNodes.add(node);
    }

    @Override
    public void removeDialogNode(AbstractDialogNode node) {
        this.dialogNodes.remove(node);
    }

    @Nullable
    @Override
    public AbstractDialogNode getCurrentNode() {
        return getDialogNodeByID(currentNodeID);
    }

    @Override
    public void setCurrentNode(AbstractDialogNode node) {
        this.currentNodeID = node.getID();
    }

    public AbstractDialogNode getDialogNodeByID(String nodeId) {
        // 添加空值检查
        if (nodeId == null || nodeId.isEmpty()) {
            System.out.println("Warning: Attempted to get dialog node with null or empty ID");
            // 返回默认节点而不是抛出异常
            return getDefaultDialogNode();
        }

        AbstractDialogNode node = dialogNodes.stream()
                .filter(n -> n.getID().equals(nodeId))
                .findFirst()
                .orElse(null);

        if (node == null) {
            System.out.println("Warning: Dialog node not found with ID: " + nodeId);
            return getDefaultDialogNode();
        }

        return node;
    }

    // 添加获取默认节点的方法
    public AbstractDialogNode getDefaultDialogNode() {
        if (!dialogNodes.isEmpty()) {
            return dialogNodes.get(0); // 返回第一个节点作为默认
        } else {
            // 创建一个默认对话节点
            return defaultDialogNode;
        }
    }





    /* ================= 存档 ================= */

    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("IsNpc", isNpc);

        CompoundNBT dialogs = new CompoundNBT();
        for (AbstractDialogNode node : dialogNodes) {
            dialogs.put(node.getID(), node.serializeNBT());
        }

        tag.put("Dialogs", dialogs);


        return tag;
    }

    @Override
    public void deserializeNBT(INBT inbt) {
        CompoundNBT tag = (CompoundNBT) inbt;
        isNpc = tag.getBoolean("IsNpc");

        this.clearDialogNodes();
        CompoundNBT dialogs = tag.getCompound("Dialogs");
        for (String key : dialogs.keySet()) {
            AbstractDialogNode node = new CommonDialogNode(); // TODO Different node types
            node.deserializeNBT(dialogs.get(key));
            this.addDialogNode(node);
        }

    }

}