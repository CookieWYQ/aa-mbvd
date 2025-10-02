package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AA_MbvdMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEntitySpawnHandler {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof ModThrowableEntity) {

        }
    }
}


