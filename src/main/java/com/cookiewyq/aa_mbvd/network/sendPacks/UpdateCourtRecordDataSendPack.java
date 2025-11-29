package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.worldSavedData.CourtRecordSaveData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class UpdateCourtRecordDataSendPack {

    private final UUID playerUUID;
    private final Inventory inventory;

    public UpdateCourtRecordDataSendPack(UUID playerUUID, Inventory inventory) {
        this.playerUUID = playerUUID;
        this.inventory = inventory;
    }

    public UpdateCourtRecordDataSendPack(PacketBuffer buffer) {
        this.playerUUID = buffer.readUniqueId();
        this.inventory = new Inventory();
        this.inventory.read(Objects.requireNonNull(buffer.readCompoundTag()).getList("Items", 10));
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeUniqueId(playerUUID);
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", inventory.write());
        buffer.writeCompoundTag(nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            if (ctx.getSender() != null) {
                ServerPlayerEntity player = ctx.getSender();
                CourtRecordSaveData courtRecordSaveData = CourtRecordSaveData.get(player.world);
                courtRecordSaveData.updateData(player.getUniqueID(), inventory);
            }
        });
        ctx.setPacketHandled(true);
    }

}
