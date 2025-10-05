package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.CourtRecordInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AA_MbvdMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerDataEvents {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            // 为玩家实体附加法庭记录能力
            event.addCapability(new ResourceLocation(AA_MbvdMod.MOD_ID, "court_record_inventory"),
                    new CourtRecordInventory.Provider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // 玩家复活/维度传送时保留能力数据
        if (event.isWasDeath()) {
            // 复制显示证据数据
            event.getOriginal().getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(oldData -> {
                event.getPlayer().getCapability(Capabilities.SHOWING_EVIDENCE_DATA_CAPABILITY).ifPresent(newData ->
                        newData.deserializeNBT(oldData.serializeNBT()));
            });

            // 复制法庭记录数据
            event.getOriginal().getCapability(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY).ifPresent(oldStore -> {
                event.getPlayer().getCapability(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY).ifPresent(newStore -> {
                    newStore.readNBT(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY, oldStore,
                            null, oldStore.writeNBT(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY, oldStore, null));
                });
            });
        }
    }
}
