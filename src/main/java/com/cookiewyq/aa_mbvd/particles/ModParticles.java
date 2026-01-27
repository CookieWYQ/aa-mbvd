package com.cookiewyq.aa_mbvd.particles;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AA_MbvdMod.MOD_ID);
    public static final RegistryObject<ParticleType<ObjectionParticleData>> objectionParticle = PARTICLE_TYPES.register("objection_particle", ObjectionParticleType::new);

    public static void registerParticleFactories(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}

