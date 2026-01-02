package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.AA_MBVD_NpcProvider;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.OpenCourtRecordPacket;
import com.cookiewyq.aa_mbvd.network.sendPacks.OpenNpcEditorPacket;
import com.cookiewyq.aa_mbvd.util.tools;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * NPC 交互事件
 */
public class NpcInteractEvent {

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract e) {
        if (e.getWorld().isRemote) return;

//        e.getTarget().getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
//            if (cap.isNpc()) {
//                // TODO：打开 NPC 对话 GUI（下一章节实现）
//                Networking.INSTANCE.sendToServer(new OpenNpcEditorPacket(e.getTarget().getEntityId()));
//            }
//        });
    }
}