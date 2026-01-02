package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.capability.court_record.CourtRecordCapability;
import com.cookiewyq.aa_mbvd.capability.court_record.CourtRecordStorage;
import com.cookiewyq.aa_mbvd.capability.court_record.ICourtRecordCapability;
import com.cookiewyq.aa_mbvd.capability.npc.AA_MBVD_NpcCapability;
import com.cookiewyq.aa_mbvd.capability.npc.AA_MBVD_NpcStorage;
import com.cookiewyq.aa_mbvd.capability.npc.IAA_MBVD_NpcCapability;
import com.cookiewyq.aa_mbvd.capability.showing_evidence.IShowingEvidenceData;
import com.cookiewyq.aa_mbvd.capability.showing_evidence.ShowingEvidenceData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Capabilities {
    @CapabilityInject(IShowingEvidenceData.class)
    public static Capability<IShowingEvidenceData> SHOWING_EVIDENCE_DATA_CAPABILITY;

    @CapabilityInject(ICourtRecordCapability.class)
    public static Capability<ICourtRecordCapability> COURT_RECORD_CAPABILITY;

    @CapabilityInject(IAA_MBVD_NpcCapability.class)
    public static Capability<IAA_MBVD_NpcCapability> AA_MBVD_NPC_CAPABILITY;



    @SubscribeEvent
    public static void registerCapabilities(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CapabilityManager.INSTANCE.register(
                    IShowingEvidenceData.class,
                    new ShowingEvidenceData.Storage(),
                    ShowingEvidenceData::new
            );

            CapabilityManager.INSTANCE.register(
                    ICourtRecordCapability.class,
                    new CourtRecordStorage(),
                    CourtRecordCapability::new
            );

            CapabilityManager.INSTANCE.register(
                    IAA_MBVD_NpcCapability.class,
                    new AA_MBVD_NpcStorage(),
                    AA_MBVD_NpcCapability::new
            );
        });
    }
}

