package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.items.evidence.EvidenceCallbackManger;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.ShowingEvidenceSendPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

public class ShowEvidence {

    // 通用方法
    public static <I extends ItemStack> void processEvidenceCallback(HudClientEvent.STATE state, I itemStack, PlayerEntity player,
                                                                     long displaying_duration) {
        PLOGGER.info("Fuck processEvidenceCallback, state now: {}", state);
        Optional.of(itemStack.getItem())
                .filter(IEvidenceCallback.class::isInstance)
                .map(IEvidenceCallback.class::cast)
                .ifPresent(callback -> {
                    PLOGGER.debug("Fuck processEvidenceCallback,In Opt,then Switch, state now: {}", state);
                    switch (state) {
                        case Pre:
                        case Post:
                            callback.onPostShowEvidence(itemStack, player);
                            break;
                        case Showing:
                            callback.onShowingEvidence(itemStack, player, displaying_duration);
                            break;
                    }
                });
    }

    public static void handleShowEvidenceSendPacket(HudClientEvent.STATE state, ItemStack itemStack, PlayerEntity player, long displaying_duration, long displayStartTime) {
        PLOGGER.debug("Fuck ShowingEvidenceSendPacket (On HudClientEvent), state now: {}", state);
        Networking.INSTANCE.sendToServer(new ShowingEvidenceSendPacket(state, itemStack, player.getUniqueID(), displaying_duration, displayStartTime));
    }

    public static void handleShowEvidenceOnServer(HudClientEvent.STATE state, ItemStack itemStack, ServerPlayerEntity player, long displaying_duration) {
        // 这里处理所有需要在服务端执行的效果
        processEvidenceCallback(state, itemStack, player, displaying_duration);

        if (!EvidenceCallbackManger.isCallback(itemStack.getItem())) return;

        PLOGGER.debug("_Handling show evidence on server, state: {}", state);

        switch (state) {
            case Pre:
                EvidenceCallbackManger.onPreShowEvidence(itemStack, player);
                break;
            case Post:
                EvidenceCallbackManger.onPostShowEvidence(itemStack, player);
                break;
            case Showing:
                EvidenceCallbackManger.onShowingEvidence(itemStack, player, displaying_duration);
                break;
            case None:
                // 处理None状态
                break;
        }
    }


    public static void handleShowEvidence(HudClientEvent.STATE state, ItemStack itemStack, PlayerEntity player, long displaying_duration) {
        // 这个方法应该在服务端执行
        processEvidenceCallback(state, itemStack, player, displaying_duration);

        if (!EvidenceCallbackManger.isCallback(itemStack.getItem())) return;

        PLOGGER.debug("Handling show evidence on server, state: {}", state);

        switch (state) {
            case Pre:
                EvidenceCallbackManger.onPreShowEvidence(itemStack, player);
                break;
            case Post:
                EvidenceCallbackManger.onPostShowEvidence(itemStack, player);
                break;
            case Showing:
                EvidenceCallbackManger.onShowingEvidence(itemStack, player, displaying_duration);
                break;
            case None:
                // 处理None状态
                break;
        }
    }

}
