package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.screen.CourtRecordScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class GetClientCourtRecordDataSendPack {
    UUID playerUUID;
    private final Inventory inventory;

    public GetClientCourtRecordDataSendPack(Inventory inventory) {
        this.inventory = inventory;
    }

    public GetClientCourtRecordDataSendPack(PacketBuffer buffer) {
        this.inventory = new Inventory();
        this.inventory.read(Objects.requireNonNull(buffer.readCompoundTag()).getList("Items", 10));
    }

    public void toBytes(PacketBuffer buffer) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", inventory.write());
        buffer.writeCompoundTag(nbt);
    }

public void handle(Supplier<NetworkEvent.Context> context) {
    NetworkEvent.Context ctx = context.get();
    ctx.enqueueWork(() -> {
        try {
            // 确保在客户端主线程执行
            Screen currentScreen = Minecraft.getInstance().currentScreen;
            if (currentScreen instanceof CourtRecordScreen) {
                CourtRecordScreen courtScreen = (CourtRecordScreen) currentScreen;
                System.out.println("Received court record data from server, updating inventory");
                courtScreen.getContainer().updateSlotsWithData(inventory);
                // 强制刷新屏幕
                courtScreen.init(Minecraft.getInstance(), courtScreen.width, courtScreen.height);
            } else {
                System.out.println("Current screen is not CourtRecordScreen: " + currentScreen);
            }
        } catch (Exception e) {
            System.err.println("Error handling GetClientCourtRecordDataSendPack: " + e.getMessage());
            e.printStackTrace();
        }
    });
    ctx.setPacketHandled(true);
}


}
