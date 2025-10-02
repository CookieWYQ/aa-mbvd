package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.cookiewyq.aa_mbvd.events.HudClientEvent.handleAutoDisplayTakethat;

public class ModForgeEvents {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        // 检查死亡实体是否是被玩家杀死的
        if (event.getSource().getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();

            ItemStack mainHandItem = player.getHeldItemMainhand();
            // 处理剑异议显示
            if (mainHandItem.getItem() instanceof SwordItem) {
                if (ModConfigs.isShowLittleMatterWhenUsingKillingEntity.get()) {
                    handleAutoDisplayTakethat(player);
                }
            }
        }
    }
}