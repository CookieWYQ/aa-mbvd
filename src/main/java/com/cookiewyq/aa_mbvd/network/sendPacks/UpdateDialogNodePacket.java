package com.cookiewyq.aa_mbvd.network.sendPacks;


import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateDialogNodePacket {
    private final int entityId;
    private final String nodeId;
    private final String roleName;
    private final String content;
    private final String nextNodeId;

    public UpdateDialogNodePacket(int entityId, String nodeId, String roleName, String content, String nextNodeId) {
        this.entityId = entityId;
        this.nodeId = nodeId;
        this.roleName = roleName;
        this.content = content;
        this.nextNodeId = nextNodeId;
    }

    public UpdateDialogNodePacket(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
        this.nodeId = buffer.readString();
        this.roleName = buffer.readString();
        this.content = buffer.readString();
        this.nextNodeId = buffer.readString();
    }


    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(entityId);
        buffer.writeString(nodeId);
        buffer.writeString(roleName);
        buffer.writeString(content);
        buffer.writeString(nextNodeId);
    }

    // getter方法
    public int getEntityId() {
        return entityId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getContent() {
        return content;
    }

    public String getNextNodeId() {
        return nextNodeId;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();
            if (sender != null && !sender.world.isRemote) {
                // 在服务端处理数据更新
                Entity targetEntity = sender.world.getEntityByID(this.entityId);
                if (targetEntity instanceof MobEntity) {
                    MobEntity mobEntity = (MobEntity) targetEntity;

                    // 获取并更新NPC能力
                    mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                        // 更新指定的对话节点
                        AbstractDialogNode node = cap.getDialogNodeByID(this.nodeId);
                        if (node != null) {
                            // 更新节点信息
                            node.setID(this.nodeId);
                            node.setRoleName(new StringTextComponent(this.roleName));
                            node.setContent(new StringTextComponent(this.content));
                            node.setNextNodeId(this.nextNodeId);

                            // 同步更新到所有客户端
                            // NetworkHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> mobEntity),
                            //     new SyncDialogNodePacket(mobEntity.getEntityId(), node));
                        }
                    });
                }
            }
        });
        context.setPacketHandled(true);
    }

}
