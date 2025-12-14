package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.ICourtRecordCapability;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainerTypes {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<ContainerType<CourtRecordContainer>>
            COURTRECORDS_CONTAINER =
            CONTAINERS.register("court_records_container",
                    () -> {
                        AA_MbvdMod.PLOGGER.info("Registering CourtRecordContainer type");
                        return IForgeContainerType.create(((windowId, inv, data) -> {
                            AA_MbvdMod.PLOGGER.info("Creating CourtRecordContainer from ModContainerTypes");
                            return new CourtRecordContainer(windowId, inv);
                        }));
                    });

    public static void register(IEventBus eventBus) {
        AA_MbvdMod.PLOGGER.info("Registering container types");
        CONTAINERS.register(eventBus);
    }
}