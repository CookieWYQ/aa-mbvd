package com.cookiewyq.aa_mbvd.enchantments;

import com.cookiewyq.aa_mbvd.items.custom.other.GantBoom;
import com.cookiewyq.aa_mbvd.items.custom.other.MetalDetector;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.Explosion;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BoomEnchantment extends Enchantment {

    protected BoomEnchantment() {
        super(Rarity.RARE, EnchantmentType.WEAPON, new EquipmentSlotType[]{EquipmentSlotType.MAINHAND});
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return enchantmentLevel * 11;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return enchantmentLevel * 11 + 20;
    }

    @Override
    public int getMaxLevel() {
        return 7;
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return itemStack.getItem() instanceof MetalDetector;
    }

    @Override
    public boolean canGenerateInLoot() {
        return true;
    }

    @Override
    public boolean canVillagerTrade() {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) {
        return this.canApply(itemStack);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return true;
    }

    @Override
    protected boolean canApplyTogether(Enchantment enchantment) {
        return super.canApplyTogether(enchantment) || enchantment.equals(Enchantments.SWEEPING);
    }

    // 当实体受到伤害时触发的回调方法（玩家攻击其他实体时）
    @Override
    public void onEntityDamaged(LivingEntity user, Entity target, int level) {
        // 检查附魔等级是否有效
        if (level > 0) {
            // 给目标实体添加击飞效果
            target.setMotion(target.getMotion().add(0, 0.05 *  level, 0));

            // 给目标实体添加发光效果（如果是生物实体）
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addPotionEffect(new EffectInstance(Effects.GLOWING, 100, 0));
            }

            if (user instanceof PlayerEntity) {
                if (user.getHeldItemOffhand().getItem() instanceof GantBoom) {
                    int gant_boom_num = user.getHeldItemOffhand().getCount();

                    if (gant_boom_num > 0) {

                        int final_level = Math.min(level, gant_boom_num);

                        user.world.createExplosion(
                                user,
                                user.getPosX(),
                                user.getPosY(),
                                user.getPosZ(),
                                final_level,
                                Explosion.Mode.DESTROY
                        );

                        if (!((PlayerEntity)user).isCreative()){
                            user.getHeldItemOffhand().setCount(gant_boom_num - final_level);
                        }
                    }
                }
            }

            // 损坏武器
            ItemStack weapon = user.getHeldItemMainhand();
            weapon.damageItem(level, user, (e) -> e.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        }
    }


    // 当用户（玩家）受到伤害时触发的回调方法
    @Override
    public void onUserHurt(LivingEntity damagedEntity, Entity sourcEntity, int damageAmount) {
        if (new Random().nextFloat() > 0.1) return;
        ItemStack itemStack = damagedEntity.getHeldItemMainhand();
        if (itemStack.getItem() instanceof MetalDetector) {
            // 使用 EnchantmentHelper 获取附魔等级
            int level = EnchantmentHelper.getEnchantmentLevel(this, itemStack);
            ItemStack i = damagedEntity.getHeldItemOffhand();
            int gant_boom_num = 0;
            if (i.getItem() instanceof GantBoom) {
                gant_boom_num = i.getCount();
            }
            if (level > 0 && gant_boom_num > 0 && i.getItem() instanceof GantBoom) {
                // 触发爆炸效果
                damagedEntity.world.createExplosion(
                        sourcEntity,
                        sourcEntity.getPosX(),
                        sourcEntity.getPosY() - 0.5,
                        sourcEntity.getPosZ(),
                        level,
                        Explosion.Mode.BREAK
                );
                if (damagedEntity instanceof PlayerEntity) {
                    damagedEntity.heal(damageAmount);
                }
                i.setCount(i.getCount() - 1);
            }
        }
    }
}
