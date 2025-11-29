package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.worldSavedData.CourtRecordSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.UUID;
import java.util.function.Supplier;

public class GetServerCourtRecordDataSendPack {

    private final UUID playerUUID;
    private Inventory courtRecordInventoryHandle;

    public GetServerCourtRecordDataSendPack(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public GetServerCourtRecordDataSendPack(PacketBuffer buffer) {
        this.playerUUID = buffer.readUniqueId();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeUniqueId(playerUUID);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

public void handle(Supplier<NetworkEvent.Context> context) {
    NetworkEvent.Context ctx = context.get();
    ctx.enqueueWork(() -> {
        if (ctx.getSender() != null){
            try {
                ServerPlayerEntity player = ctx.getSender();
                CourtRecordSaveData courtRecordSaveData = CourtRecordSaveData.get(player.world);
                Inventory inventoryData;
                try {
                    inventoryData = courtRecordSaveData.getData(playerUUID);
                } catch (IllegalArgumentException e) {
                    // 如果没有数据，则创建新的空inventory
                    System.out.println("No existing data found for player, creating new inventory");
                    inventoryData = new Inventory(com.cookiewyq.aa_mbvd.configs.ModConfigs.COURT_RECORD_ROWS.get() * 9);
                }
                System.out.println("Sending court record data to client");
                Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player),
                                       new GetClientCourtRecordDataSendPack(inventoryData));
            } catch (Exception e) {
                System.err.println("Error in GetServerCourtRecordDataSendPack: " + e.getMessage());
                e.printStackTrace();
            }
        }
    });
    ctx.setPacketHandled(true);
}


}
