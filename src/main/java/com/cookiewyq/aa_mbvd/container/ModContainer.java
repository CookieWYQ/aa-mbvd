package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainer {

    public static DeferredRegister<ContainerType<?>> CONTAINERS
            = DeferredRegister.create(ForgeRegistries.CONTAINERS, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<ContainerType<CourtRecordsContainer>>
            COURTRECORDS_CONTAINER =
            CONTAINERS.register("court_records_container",
                    () -> IForgeContainerType.create(((windowId, inv, data) -> {
                        BlockPos pos = data.readBlockPos();
                        World world = inv.player.getEntityWorld();
                        return new CourtRecordsContainer(windowId, world, pos, inv, inv.player);
                    }
                    )));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
