package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.capability.Capabilities;
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
                    () -> IForgeContainerType.create(((windowId, inv, data) -> {
                        IItemHandlerModifiable handler = (IItemHandlerModifiable) inv.player.getCapability(Capabilities.COURT_RECORD_INVENTORY_CAPABILITY)
                                .orElseThrow(() -> new IllegalStateException("Court record capability not found"));
                        return new CourtRecordContainer(windowId, inv, handler);
                    }
                    )));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
