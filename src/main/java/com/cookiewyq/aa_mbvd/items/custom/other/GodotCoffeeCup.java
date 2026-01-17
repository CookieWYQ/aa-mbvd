package com.cookiewyq.aa_mbvd.items.custom.other;

import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.custom.badges.ModThrowableItem;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.tools;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GodotCoffeeCup extends ModThrowableItem {

    public GodotCoffeeCup() {
        super(
                new Properties()
                        .group(ModItemGroup.AA_MBVD_TAB)
                        .rarity(Rarity.RARE)
                        .maxStackSize(17)
        );
    }

    @Override
    public boolean isRemoveOnImpact(ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slotId, boolean isSelected) {

    }

    @Override
    public void playSound(World world, PlayerEntity player) {
        world.playSound(player, player.getPosition(), ModSounds.GODOT_COFFEE_IS_A_DARK_FRAGRANCE.get(), SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    @Override
    public void onEntityHit(EntityRayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        if (rayTraceResult.getEntity() instanceof LivingEntity) {
            LivingEntity target = (LivingEntity) rayTraceResult.getEntity();
            target.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 250, 2));
            player.sendMessage(new TranslationTextComponent("message.godot_coffee_cup", tools.getTranslatedEntityName(target)), player.getUniqueID());
        }
    }

    @Override
    public void onImpact(RayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {

    }
}
