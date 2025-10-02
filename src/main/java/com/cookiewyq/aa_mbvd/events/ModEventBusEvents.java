package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.IShowingEvidenceData;
import com.cookiewyq.aa_mbvd.capability.ShowingEvidenceData;
import com.cookiewyq.aa_mbvd.entities.ModEntityTypes;
import com.cookiewyq.aa_mbvd.entities.custom.PhoenixWrightEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = AA_MbvdMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        // 为PhoenixWrightEntity注册默认属性
        event.put(ModEntityTypes.PhoenixWright.get(), PhoenixWrightEntity.setCustomAttributes().create());
    }

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        // 实体类型注册完成后才初始化生成蛋
        // 这里不需要做任何事情，因为我们会在FMLCommonSetupEvent中处理
    }
}
