package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.particles.ModParticles;
import com.cookiewyq.aa_mbvd.particles.ObjectionParticleFactory;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ParticleFactoryRegistry {

    @SubscribeEvent
    public static void onParticleFactoryRegistration(ParticleFactoryRegisterEvent event) {
        System.out.println("Registering particle factory for objection particle...");
        Minecraft.getInstance().particles.registerFactory(ModParticles.objectionParticle.get(), ObjectionParticleFactory::new);
        System.out.println("Particle factory registered successfully!");
    }
    
    // 对于1.16.5，使用FMLClientSetupEvent替代ParticleFactoryRegisterEvent
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        System.out.println("FML Client Setup Event triggered - registering particle factory");
        event.enqueueWork(() -> {
            Minecraft.getInstance().particles.registerFactory(ModParticles.objectionParticle.get(), ObjectionParticleFactory::new);
            System.out.println("Objection particle factory registered via FMLClientSetupEvent");
        });
    }
    
    // 粒子工厂现在在AA_MbvdMod类的doClientStuff方法中注册
    // 保留此类作为粒子相关事件的占位符，如有需要可添加其他事件
}