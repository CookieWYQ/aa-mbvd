package com.cookiewyq.aa_mbvd.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
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

    @CapabilityInject(Configable.class)
    public static Capability<Configable> CONFIGABLE_CAPABILITY;

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
                    Configable.class,
                    new Configable.Storage(),
                    Configable::new
            );
        });
    }
}

