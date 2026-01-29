package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nullable;
import java.util.*;

/**
 * NPC 能力
 * 挂载在 LivingEntity 上
 */
public class AA_MBVD_NpcCapability implements IAA_MBVD_NpcCapability {

    /** 是否为 NPC */
    private boolean isNpc = false;

    private final ArrayList<AbstractDialogNode> dialogNodes = new ArrayList<>();

    private String currentNodeID;

    public static final CommonDialogNode defaultDialogNode = new CommonDialogNode();

    public boolean isNpc() {
        return isNpc;
    }

    public void setNpc(boolean npc) {
        this.isNpc = npc;
    }

    @Override
    public boolean isDialogNodeExists(String nodeId) {
        for (AbstractDialogNode node : dialogNodes) {
            if (node.getID().equals(nodeId)) {
                return true;
            }
        }
        return false;
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
    public boolean addDialogNode(AbstractDialogNode node) {
        if (node == null) {
            return false;
        }
        // 检查ID是否为空
        if (node.getID() == null || node.getID().isEmpty()) {
            System.out.println("Warning: Attempted to add dialog node with null or empty ID");
            return false;
        }
        // 检查ID是否已存在
        if (isDialogNodeExists(node.getID())) {
            System.out.println("Warning: Dialog node with ID already exists: " + node.getID());
            return false;
        }
        this.dialogNodes.add(node);
        return true;
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
            return null;
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

    // 如果没有对话节点，添加默认节点
    public void ensureHasDialogNodes() {
        if (dialogNodes.isEmpty()) {
            dialogNodes.add(defaultDialogNode);
        }
    }


    /* ================= 存档 ================= */

    // 在 AA_MBVD_NpcCapability 的 serializeNBT 方法中
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("IsNpc", isNpc);

        CompoundNBT dialogs = new CompoundNBT();
        for (AbstractDialogNode node : dialogNodes) {
            dialogs.put(node.getID(), node.serializeNBT());
        }
        tag.put("Dialogs", dialogs);

        // 添加当前节点ID的持久化
        if (currentNodeID != null) {
            tag.putString("CurrentNodeID", currentNodeID);
        } else {
            tag.putString("CurrentNodeID", ""); // 确保即使为null也能保存
        }

        return tag;
    }

    // 在 AA_MBVD_NpcCapability 的 deserializeNBT 方法中
    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        isNpc = nbt.getBoolean("IsNpc");

        this.clearDialogNodes();
        CompoundNBT dialogs = nbt.getCompound("Dialogs");
        for (String key : dialogs.keySet()) {
            AbstractDialogNode node = new CommonDialogNode(); // TODO Different node types
            node.deserializeNBT(dialogs.get(key));
            this.addDialogNode(node);
        }

        // 读取当前节点ID
        if (nbt.contains("CurrentNodeID")) {
            this.currentNodeID = nbt.getString("CurrentNodeID");
            // 验证当前节点是否存在
            if (getDialogNodeByID(this.currentNodeID) == null && !dialogNodes.isEmpty()) {
                // 如果当前节点不存在，设置为第一个节点
                this.currentNodeID = dialogNodes.get(0).getID();
            }
        } else {
            // 如果没有保存的当前节点ID，默认设为第一个节点
            if (!dialogNodes.isEmpty()) {
                this.currentNodeID = dialogNodes.get(0).getID();
            }
        }

        // 确保至少有一个节点
        ensureHasDialogNodes();
    }


}