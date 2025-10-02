package com.cookiewyq.aa_mbvd.tileentity;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = 
        DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<TileEntityType<CourtRecordsTileEntity>> COURT_RECORDS_TILE_ENTITY = 
        TILE_ENTITIES.register("court_records_tile_entity", 
            () -> TileEntityType.Builder.create(CourtRecordsTileEntity::new).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
