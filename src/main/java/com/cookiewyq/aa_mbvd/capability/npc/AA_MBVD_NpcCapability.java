package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * NPC 能力
 * 挂载在 LivingEntity 上
 */
public class AA_MBVD_NpcCapability implements IAA_MBVD_NpcCapability {

    /** 是否为 NPC */
    private boolean isNpc = false;

    /** NPC 显示名称（支持翻译键） */
    private ITextComponent npcName =
            new TranslationTextComponent("npc.aa_mbvd.default");

    /** 对话内容列表 */
    private final List<AbstractDialogNode> dialogs = new ArrayList<>(Collections.nCopies(1, new CommonDialogNode()));

    private Map<UUID, AbstractDialogNode> dialogMap = new HashMap<>();

    public boolean isNpc() {
        return isNpc;
    }

    public void setNpc(boolean npc) {
        this.isNpc = npc;
    }

    public ITextComponent getNpcName() {
        return npcName;
    }

    public void setNpcName(ITextComponent npcName) {
        this.npcName = npcName;
    }

    public List<AbstractDialogNode> getDialogNodes() {
        return dialogs;
    }

    @Override
    public void clearDialogNodes() {
        dialogs.clear();
    }

    @Override
    public void setDialogNodeMap(Map<UUID, AbstractDialogNode> dialogMap) {
        this.dialogMap = dialogMap;
    }

    public void addDialogNode(AbstractDialogNode node) {
        dialogs.add(node);
        updateDialogMap();
    }

    @Nullable
    public AbstractDialogNode getDialogNode(UUID uuid) {
        return dialogMap.get(uuid);
    }

    public AbstractDialogNode getCurrentNode() {
        if (dialogs.isEmpty()) {
            // 如果没有节点，创建一个默认节点
            AbstractDialogNode defaultNode = new CommonDialogNode();
            dialogs.add(defaultNode);
            updateDialogMap(); // 更新映射
        }
        return dialogs.get(0);
    }

    private void updateDialogMap() {
        dialogMap = dialogs.stream()
                .filter(Objects::nonNull)
                .filter(node -> node.getUUID() != null)
                .collect(Collectors.toMap(AbstractDialogNode::getUUID, node -> node));
    }




    /* ================= 存档 ================= */

    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("IsNpc", isNpc);

        // 添加安全检查
        if (npcName != null) {
            tag.putString("NpcName", ITextComponent.Serializer.toJson(npcName));
        } else {
            tag.putString("NpcName", ITextComponent.Serializer.toJson(
                    new TranslationTextComponent("npc.aa_mbvd.default")));
        }

        // 过滤掉null对象并计算有效对话数量
        List<AbstractDialogNode> validDialogs = dialogs.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        tag.putInt("DialogCount", validDialogs.size());

        // 只序列化非null的对话节点
        for (AbstractDialogNode dialog : validDialogs) {
            if (dialog != null && dialog.getUUID() != null) {
                tag.put(dialog.getUUID().toString(), dialog.serializeNBT());
            }
        }
        return tag;
    }

    @Override
    public void deserializeNBT(INBT inbt) {
        CompoundNBT tag = (CompoundNBT) inbt;
        isNpc = tag.getBoolean("IsNpc");

        if (tag.contains("NpcName")) {
            ITextComponent deserializedName = ITextComponent.Serializer.getComponentFromJson(tag.getString("NpcName"));
            if (deserializedName != null) {
                npcName = deserializedName;
            } else {
                npcName = new TranslationTextComponent("npc.aa_mbvd.default");
            }
        }

        dialogs.clear();

        // 修复反序列化逻辑 - 遍历所有可能的UUID键
        Set<String> keys = tag.keySet();
        for (String key : keys) {
            if (!key.equals("IsNpc") && !key.equals("NpcName") && !key.equals("DialogCount")) {
                try {
                    UUID nodeUUID = UUID.fromString(key);
                    CompoundNBT dialogTag = tag.getCompound(key);
                    AbstractDialogNode node = new CommonDialogNode();
                    node.deserializeNBT(dialogTag);
                    dialogs.add(node);
                } catch (IllegalArgumentException ignored) {
                    // 如果键不是UUID格式，跳过
                }
            }
        }

        // 重新构建dialogMap
        dialogMap = dialogs.stream()
                .filter(Objects::nonNull)
                .filter(node -> node.getUUID() != null)
                .collect(Collectors.toMap(AbstractDialogNode::getUUID, node -> node));
        
        // 确保至少有一个对话节点
        if (dialogs.isEmpty()) {
            dialogs.add(new CommonDialogNode());
        }
    }

}