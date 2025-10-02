// 文件: src/main/java/com/cookiewyq/cookiemod/network/sendPacks/LittleMatterSendPacket.java
package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.function.Supplier;

public class LittleMatterSendPacket {
    private final String word;
    private final String role;
    private final String lang;
    private final double x, y, z;

    public LittleMatterSendPacket(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang, double x, double y, double z) {
        this.word = word.getId();
        this.role = role.getId();
        this.lang = lang.getId();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LittleMatterSendPacket(PacketBuffer buffer) {
        this.word = buffer.readString(32767);
        this.role = buffer.readString(32767);
        this.lang = buffer.readString(32767);
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.word);
        buffer.writeString(this.role);
        buffer.writeString(this.lang);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();

        // 在服务端执行逻辑
        ctx.enqueueWork(() -> {
            // 获取发送此包的玩家
            ServerPlayerEntity sender = ctx.getSender();
            if (sender != null) {
                World world = sender.world;

                // 获取声音事件
                LittleMatter_Words wordEnum = LittleMatter_Words.getWord(word);
                LittleMatter_Roles roleEnum = LittleMatter_Roles.getRole(role);
                LittleMatter_Langs langEnum = LittleMatter_Langs.getLang(lang);

                if (wordEnum != null && roleEnum != null && langEnum != null) {
                    // 获取声音
                    RegistryObject<SoundEvent> sound = ModSounds.getLittleMatterSound(wordEnum, roleEnum, langEnum);

                    if (sound != null && sound.isPresent()) {
                        // 在指定位置播放声音，让附近所有玩家都能听到
                        world.playSound(
                                null, // 不针对特定玩家
                                x, y, z, // 声音位置
                                sound.get(),
                                SoundCategory.PLAYERS,
                                1.0F,
                                1.0F
                        );

                        // 向附近所有玩家发送显示头顶图片的包
                        LittleMatterHeadDisplayPacket displayPacket = new LittleMatterHeadDisplayPacket(
                                wordEnum, roleEnum, langEnum, sender.getUniqueID(), x, y, z
                        );

                        // 发送给所有附近的玩家
                        for (PlayerEntity player : world.getPlayers()) {
                            if (player.getDistanceSq(x, y, z) < 256.0D) { // 16格范围内的玩家
                                Networking.INSTANCE.send(
                                        PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                                        displayPacket
                                );
                            }
                        }
                    }
                }
            }
        });

        ctx.setPacketHandled(true);
    }


    // Getters
    public String getWord() {
        return word;
    }

    public String getRole() {
        return role;
    }

    public String getLang() {
        return lang;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
