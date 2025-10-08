package com.cookiewyq.aa_mbvd.network.sendPacks;


import com.cookiewyq.aa_mbvd.container.CourtRecordContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

public class OpenCourtRecordScreenPacket {
    public OpenCourtRecordScreenPacket() {
        // 空构造函数用于创建新实例
    }

    public OpenCourtRecordScreenPacket(PacketBuffer buffer) {
        // 从缓冲区读取数据（此包不需要数据）
    }

    public static OpenCourtRecordScreenPacket decode(PacketBuffer buffer) {
        return new OpenCourtRecordScreenPacket(buffer);
    }

    public void encode(PacketBuffer buffer) {
        // 编码数据到缓冲区（此包不需要数据）
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            // 在服务端打开GUI
            net.minecraft.entity.player.ServerPlayerEntity player = context.getSender();
            if (player != null) {
                net.minecraftforge.fml.network.NetworkHooks.openGui(
                    player,
                    new CourtRecordContainerProvider(),
                    buf -> {}
                );
            }
        });
        context.setPacketHandled(true);
    }
}
