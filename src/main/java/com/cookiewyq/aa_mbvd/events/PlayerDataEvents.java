package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerDataEvents {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // 玩家复活/维度传送时保留能力数据
        if (event.isWasDeath()) {
            // 获取旧玩家的能力数据
            event.getOriginal().getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(oldData -> {
                // 将数据复制到新玩家
                event.getPlayer().getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(newData -> newData.deserializeNBT(oldData.serializeNBT()));
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLoadFromFile(PlayerEvent.LoadFromFile event) {
        // 玩家数据从文件加载时的处理
    }

    @SubscribeEvent
    public static void onPlayerSaveToFile(PlayerEvent.SaveToFile event) {
        // 玩家数据保存到文件时的处理
    }
}
