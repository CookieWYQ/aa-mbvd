// 文件: src/main/java/com/cookiewyq/cookiemod/network/sendPacks/LittleMatterHeadDisplayPacket.java
package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.screen.LittleMatterDisplayManager;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class LittleMatterHeadDisplayPacket {
    private final String word;
    private final String role;
    private final String lang;
    private final UUID playerUUID;
    private final double x, y, z;

    public LittleMatterHeadDisplayPacket(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang, UUID playerUUID, double x, double y, double z) {
        this.word = word.getId();
        this.role = role.getId();
        this.lang = lang.getId();
        this.playerUUID = playerUUID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LittleMatterHeadDisplayPacket(PacketBuffer buffer) {
        this.word = buffer.readString(32767);
        this.role = buffer.readString(32767);
        this.lang = buffer.readString(32767);
        this.playerUUID = buffer.readUniqueId();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.word);
        buffer.writeString(this.role);
        buffer.writeString(this.lang);
        buffer.writeUniqueId(this.playerUUID);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();
        ctx.enqueueWork(() -> {
            // 在客户端处理显示逻辑
            // 这里可以存储显示信息，然后在渲染事件中使用
            // 我们可以创建一个管理器来处理这些显示请求
            LittleMatterDisplayManager.addDisplayRequest(
                playerUUID,
                LittleMatter_Words.getWord(word),
                LittleMatter_Roles.getRole(role),
                LittleMatter_Langs.getLang(lang),
                new Vector3d(x, y, z),
                System.currentTimeMillis()
            );
        });
        ctx.setPacketHandled(true);
    }

    // Getters
    public String getWord() { return word; }
    public String getRole() { return role; }
    public String getLang() { return lang; }
    public UUID getPlayerUUID() { return playerUUID; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
}
