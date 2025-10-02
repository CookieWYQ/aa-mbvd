package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.enchantments.ModEnchantments;
import com.cookiewyq.aa_mbvd.items.custom.other.MetalDetector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import static com.cookiewyq.aa_mbvd.events.HudClientEvent.handleAutoDisplayTakethat;

@Mod.EventBusSubscriber(modid = AA_MbvdMod.MOD_ID)
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

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
//        if (new Random().nextFloat() > 0.3) return;
        System.out.println("=== LivingHurtEvent DEBUG ===");
        LivingEntity entity = event.getEntityLiving();
        DamageSource source = event.getSource();
        System.out.println("Entity: " + entity.getName() + ", Damage: " + event.getAmount());

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            ItemStack itemStack = player.getHeldItemMainhand();
            System.out.println("Player holding: " + itemStack.getItem().getRegistryName());

            // 检查是否有BOOM附魔
            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BOOM_ENCHANTMENT.get(), itemStack);
            System.out.println("Boom enchantment level: " + level);

            if (source.isExplosion() && itemStack.getItem() instanceof MetalDetector && level > 0) {
                System.out.println("Applying explosion damage reduction");
                event.setAmount(1);
                itemStack.damageItem((int) (event.getAmount() / 10), player, (e) -> e.sendBreakAnimation(Hand.MAIN_HAND));
            } else {
                System.out.println("Condition not met - source.isExplosion(): " + source.isExplosion() +
                        ", is MetalDetector: " + (itemStack.getItem() instanceof MetalDetector) +
                        ", level > 0: " + (level > 0));
            }
        } else if (source.getTrueSource() != null && source.getTrueSource() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) source.getTrueSource();
            ItemStack itemStack = player.getHeldItemMainhand();
            System.out.println("Player attacking with: " + itemStack.getItem().getRegistryName());

            int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BOOM_ENCHANTMENT.get(), itemStack);
            System.out.println("Boom enchantment level: " + level);

            if (level > 0) {
                System.out.println("Applying boom effect");
                entity.setMotion(0, 1, 0);
                entity.addPotionEffect(new EffectInstance(Effects.GLOWING, 100, 0));
                player.world.createExplosion(
                        entity,
                        entity.getPosX(),
                        entity.getPosY(),
                        entity.getPosZ(),
                        level,
                        Explosion.Mode.DESTROY
                );
                itemStack.damageItem(level, player, (e) -> e.sendBreakAnimation(Hand.MAIN_HAND));
            } else {
                System.out.println("No boom enchantment found");
            }
        }
        System.out.println("=== End LivingHurtEvent DEBUG ===");
    }

}