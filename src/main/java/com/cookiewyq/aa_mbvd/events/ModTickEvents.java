package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.ShowingEvidenceData;
import com.cookiewyq.aa_mbvd.util.tools;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;
import static com.cookiewyq.aa_mbvd.events.HudClientEvent.getShowingEvidencePlayerUUID;

@Mod.EventBusSubscriber
public class ModTickEvents {

//    private static boolean wasShowItemKeyPressed = false;
//    private static ItemStack displayedItem = ItemStack.EMPTY;
//    private static long displayingItemTime = 0;
//    private static final STATEHolder stateHolder = new STATEHolder();

    private static final Map<UUID, ShowingEvidenceData> playerEvidenceData = new HashMap<>();
    private static final Map<UUID, PlayerEntity> playerData = new HashMap<>();


    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            // 在客户端每个tick开始时执w行
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null) {
                // 执行相关操作
            }
        }
    }

    @Nullable
    public static PlayerEntity getPlayerByUUID(UUID uuid) {
        return playerData.get(uuid);
    }

    public static void handlePlayerShowEvidence(UUID playerUUID, HudClientEvent.STATE state,
                                                ItemStack itemStack, long displayingDuration) {
        // 这个方法由网络包调用
        PlayerEntity player = tools.getPlayerEntityFromUUIDByTickEvent(playerUUID);
        if (player != null) {
            ShowEvidence.handleShowEvidence(state, itemStack, player, displayingDuration);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PlayerEntity player = event.player;

            if (player.world.isRemote) {
//                stateHolder.setState(HudClientEvent.stateHolder.getState());
//                displayedItem = getDisplayedItem();
//                displayingItemTime = getDisplayingItemTime();
            }

            if (!player.world.isRemote) {

                if (getShowingEvidencePlayerUUID() == null) return;

                if (player.getUniqueID() != getShowingEvidencePlayerUUID()) return;

                if (!playerData.containsKey(player.getUniqueID())) {
                    playerData.put(player.getUniqueID(), player);
                }

                // 使用能力系统检查是否需要处理展示证据
                player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
                    if (data.isShowEvidence() && data.getState() != HudClientEvent.STATE.None && data.getDisplayingEvidencePlayer() != null) {
                        // 在服务端处理实际效果
                        PLOGGER.debug("Fucking Handle Show Evidence on {}, State: {}, Item: {}, Player: {}, Time: {}", player.world.isRemote ? "Client" : "Server", data.getState(), data.getDisplayedItem(), data.getDisplayingEvidencePlayer(), data.getDisplayingItemTime());
                        ShowEvidence.handleShowEvidence(
                                data.getState(),
                                data.getDisplayedItem(),
                                data.getDisplayingEvidencePlayer(),
                                data.getDisplayingItemTime()
                        );
                        // 处理完成后重置状态
                        data.setShowEvidence(false);
                        data.setState(HudClientEvent.STATE.None);
                        data.setDisplayedItem(ItemStack.EMPTY);
                        data.setDisplayingItemTime(0);
                        data.setDisplayingEvidencePlayer(null);
                    }
                });


//                if (getShowingEvidencePlayerUUID() == null) return;
//
//                if (player.getUniqueID() != getShowingEvidencePlayerUUID()) return;
//
//                PLOGGER.info("State on Server: {}", stateHolder.getState());
//
//                handleShowEvidence(stateHolder.getState(), displayedItem, player, displayingItemTime);
//                stateHolder.setState(STATE.None);


//                // 处理出示证物按键按下事件
//                boolean isShowItemKeyPressed = isIsShowItemKeyPressed();
//
//                // 只有在没有显示任何内容时才能显示新物品
//                if (isShowItemKeyPressed && !isDisplayingAnything()) {
//                    if (!wasShowItemKeyPressed) {
//                        // 按键刚刚被按下，记录开始时间和物品
//
//                        PLOGGER.debug("Pre on Server.");
//
//                        handleShowEvidence(HudClientEvent.STATE.Pre, getDisplayedItem(), player, getDisplayingItemTime());
//                    }
//                }
//
//                wasShowItemKeyPressed = isShowItemKeyPressed;
//
//                // 显示物品HUD（持续2秒）
//                if (isDisplayingItem()) {
//
//                    PLOGGER.debug("Showing on Server.");
//
//                    handleShowEvidence(HudClientEvent.STATE.Showing, getDisplayedItem(), player, getDisplayingItemTime());
//                } else if (!getDisplayedItem().isEmpty() && getDisplayingItemTime() >= 2000) {
//
//                    PLOGGER.debug("Post on Server.");
//
//                    // 2秒后清除显示的物品
//                    handleShowEvidence(HudClientEvent.STATE.Post, getDisplayedItem(), player, getDisplayingItemTime());
//                }


//                if (HudClientEvent.isShowEvidence.get()) {
//                    HudClientEvent.STATE s = HudClientEvent.stateHolder.getState();
//
//                    HudClientEvent.handleShowEvidence(
//                            s,
//                            HudClientEvent.getDisplayedItem(),
//                            player,
//                            HudClientEvent.getDisplayingItemTime()
//                    );
//                    HudClientEvent.isShowEvidence.set(false);
//                    HudClientEvent.stateHolder.setState(HudClientEvent.STATE.None);
//                }
//                // 安全地使用ifPresent避免直接调用get()
//                player.getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(data -> {
//                    if (data.isShowEvidence()) {
//                        PLOGGER.debug("Fuck cap is dealing with player tick event");
//                        HudClientEvent.handleShowEvidence(
//                                data.getState(),
//                                HudClientEvent.getDisplayedItem(),
//                                player,
//                                HudClientEvent.getDisplayingItemTime()
//                        );
//                        data.setShowEvidence(false);
//                        data.setState(HudClientEvent.STATE.None);
//                    }
//                });
//                if (HudClientEvent.isShowEvidence.get()) HudClientEvent.isShowEvidence.set(false);
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            // 在世界每个tick开始时执行
            World world = event.world;
            if (!world.isRemote) {

            }
        }
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
//            // 执行服务端任务
//            ServerTaskManager.executeTasks();
        }
    }
}
