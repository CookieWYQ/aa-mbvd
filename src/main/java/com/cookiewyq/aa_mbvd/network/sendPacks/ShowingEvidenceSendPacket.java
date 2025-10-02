package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.events.HudClientEvent;
import com.cookiewyq.aa_mbvd.events.ShowEvidence;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;


public class ShowingEvidenceSendPacket {

    private final HudClientEvent.STATE state;
    private final ItemStack itemStack;
    private final long displaying_duration;
    private final UUID player_uuid;
    private final long displayStartTime; // 添加开始时间

    public ShowingEvidenceSendPacket(HudClientEvent.STATE state, ItemStack itemStack, UUID player_uuid,
                                     long displaying_duration, long displayStartTime) {
        this.state = state;
        this.itemStack = itemStack;
        this.displaying_duration = displaying_duration;
        this.player_uuid = player_uuid;
        this.displayStartTime = displayStartTime;
    }

    public ShowingEvidenceSendPacket(PacketBuffer buffer) {
        this.state = buffer.readEnumValue(HudClientEvent.STATE.class);
        this.itemStack = buffer.readItemStack();
        this.displaying_duration = buffer.readLong();
        this.displayStartTime = buffer.readLong();
        this.player_uuid = buffer.readUniqueId();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeEnumValue(this.state);
        buffer.writeItemStack(this.itemStack);
        buffer.writeLong(this.displaying_duration);
        buffer.writeLong(this.displayStartTime);
        buffer.writeUniqueId(this.player_uuid);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity sender = context.getSender();
            if (sender != null && !sender.world.isRemote) {
                // 使用网络包中传递的确切数据
                ShowEvidence.handleShowEvidence(this.state, this.itemStack, sender, this.displaying_duration);
            }
        });
        context.setPacketHandled(true);
    }
}
