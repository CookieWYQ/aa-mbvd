package com.cookiewyq.aa_mbvd.entities;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import com.cookiewyq.aa_mbvd.entities.custom.PhoenixWrightEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, AA_MbvdMod.MOD_ID);

    public static final RegistryObject<EntityType<ModThrowableEntity>> BADGE =
        ENTITY_TYPES.register("badge_entity",
            () -> EntityType.Builder.<ModThrowableEntity>create(
                    ModThrowableEntity::new,
                    EntityClassification.MISC)
                .size(0.25F, 0.25F)
                .trackingRange(4)
                .updateInterval(10)
                .build(new ResourceLocation(AA_MbvdMod.MOD_ID, "badge_entity").toString()));

    public static final RegistryObject<EntityType<PhoenixWrightEntity>> PhoenixWright =
            ENTITY_TYPES.register("phoenix_wright",
                    () -> EntityType.Builder.create(
                                    PhoenixWrightEntity::new,
                                    EntityClassification.MISC)
                            .size(1F, 2F)
                            .trackingRange(4)
                            .updateInterval(10)
                            .build(new ResourceLocation(AA_MbvdMod.MOD_ID, "phoenix_wright").toString()));



    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
