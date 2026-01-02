package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.court_record.CourtRecordProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerCapEventHandler {
    private static final ResourceLocation ID =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "player_inventory");

    @SubscribeEvent
    public void attachCaps(AttachCapabilitiesEvent<PlayerEntity> e) {
        e.addCapability(ID, new CourtRecordProvider());
    }

    @SubscribeEvent
    public void clone(PlayerEvent.Clone e) {
        e.getOriginal().getCapability(Capabilities.COURT_RECORD_CAPABILITY).ifPresent(oldCap -> {
            e.getPlayer().getCapability(Capabilities.COURT_RECORD_CAPABILITY).ifPresent(newCap -> {
                newCap.getInventory().deserializeNBT(
                        oldCap.getInventory().serializeNBT()
                );
            });
        });
    }
}
