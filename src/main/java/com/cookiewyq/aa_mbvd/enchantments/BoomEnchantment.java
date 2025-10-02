package com.cookiewyq.aa_mbvd.enchantments;

import com.cookiewyq.aa_mbvd.items.custom.other.GantBoom;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
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
        return itemStack.getItem() instanceof SwordItem;
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

    // 当实体受到伤害时触发的回调方法
    @Override
    public void onEntityDamaged(LivingEntity damagedEntity, Entity Sourcentity, int damageAmount) {

    }

    // 当用户（玩家）受到伤害时触发的回调方法
    @Override
    public void onUserHurt(LivingEntity damagedEntity, Entity sourcEntity, int damageAmount) {
        if (new Random().nextFloat() > 0.1) return;
        ItemStack itemStack = damagedEntity.getHeldItemMainhand();
        if (itemStack.getItem() instanceof SwordItem) {
            // 使用 EnchantmentHelper 获取附魔等级
            int level = EnchantmentHelper.getEnchantmentLevel(this, itemStack);
            ItemStack i = damagedEntity.getHeldItemOffhand();
            int gant_boom_num = 0;
            if (i.getItem() instanceof GantBoom) {
                gant_boom_num = i.getCount();
            }
            if (level > 0 && gant_boom_num > 0 && i.getItem() instanceof GantBoom) {
                int n = Math.min(level, gant_boom_num);
                // 触发爆炸效果
                damagedEntity.world.createExplosion(
                        sourcEntity,
                        sourcEntity.getPosX(),
                        sourcEntity.getPosY() - 0.5,
                        sourcEntity.getPosZ(),
                        n,
                        Explosion.Mode.BREAK
                );
                i.setCount(i.getCount() - n);
            }
        }
    }
}
