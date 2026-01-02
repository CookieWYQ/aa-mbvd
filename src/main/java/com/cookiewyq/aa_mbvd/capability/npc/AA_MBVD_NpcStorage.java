package com.cookiewyq.aa_mbvd.capability.npc;

import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.stream.Collectors;

public class AA_MBVD_NpcStorage implements Capability.IStorage<IAA_MBVD_NpcCapability> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IAA_MBVD_NpcCapability> capability, IAA_MBVD_NpcCapability iaaMbvdNpcCapability, Direction direction) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("IsNpc", iaaMbvdNpcCapability.isNpc());
        tag.putString("NpcName", ITextComponent.Serializer.toJson(iaaMbvdNpcCapability.getNpcName()));

        tag.putInt("DialogCount", iaaMbvdNpcCapability.getDialogNodes().size());
        for (AbstractDialogNode dialog : iaaMbvdNpcCapability.getDialogNodes()) {
            tag.put(dialog.getUUID().toString(), dialog.serializeNBT());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IAA_MBVD_NpcCapability> capability, IAA_MBVD_NpcCapability iaaMbvdNpcCapability, Direction direction, INBT inbt) {
        CompoundNBT tag = (CompoundNBT) inbt;
        iaaMbvdNpcCapability.setNpc(tag.getBoolean("IsNpc"));

        if (tag.contains("NpcName")) {
            iaaMbvdNpcCapability.setNpcName(ITextComponent.Serializer.getComponentFromJson(tag.getString("NpcName")));
        }

        iaaMbvdNpcCapability.clearDialogNodes();
        int count = tag.getInt("DialogCount");

        // 修复反序列化逻辑
        for (String key : tag.keySet()) {
            if (!key.equals("IsNpc") && !key.equals("NpcName") && !key.equals("DialogCount")) {
                try {
                    UUID nodeUUID = UUID.fromString(key);
                    AbstractDialogNode node = new CommonDialogNode();
                    node.deserializeNBT(tag.getCompound(key));
                    iaaMbvdNpcCapability.addDialogNode(node);
                } catch (IllegalArgumentException ignored) {
                    // 如果键不是UUID格式，跳过
                }
            }
        }

        iaaMbvdNpcCapability.setDialogNodeMap(iaaMbvdNpcCapability
                .getDialogNodes()
                .stream()
                .collect(Collectors.toMap(AbstractDialogNode::getUUID, node -> node)));
        
        // 确保至少有一个对话节点
        if (iaaMbvdNpcCapability.getDialogNodes().isEmpty()) {
            iaaMbvdNpcCapability.addDialogNode(new CommonDialogNode());
        }
    }

}
