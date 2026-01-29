package com.cookiewyq.aa_mbvd.network.sendPacks;


import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

public class UpdateAllDialogNodePacket {
    private final int entityId;

    public UpdateAllDialogNodePacket(int entityId) {
        this.entityId = entityId;
    }

    public UpdateAllDialogNodePacket(PacketBuffer buffer) {
        this.entityId = buffer.readInt();
    }


    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(entityId);
    }

    // getter方法
    public int getEntityId() {
        return entityId;
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();
            if (sender != null && !sender.world.isRemote) {
                Entity targetEntity = sender.world.getEntityByID(this.entityId);
                if (targetEntity instanceof MobEntity) {
                    MobEntity mobEntity = (MobEntity) targetEntity;
                    mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                        mobEntity.getPersistentData().put("Dialogs", cap.serializeNBT());
                        PLOGGER.debug("!!!FUCK All Save Data: {}", cap.serializeNBT());
                    });

                }
            }
        });
        context.setPacketHandled(true);
    }
}
