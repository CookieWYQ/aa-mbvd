package com.cookiewyq.aa_mbvd.items.custom.other;

import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.tools;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MetalDetector extends SwordItem implements IEvidenceCallback {

    private LivingEntity target;
    private Vector3d targetPos;
    private boolean hasPlayed = false;

    public MetalDetector() {
        super(ItemTier.IRON,
                0, 0, new Properties()
                        .group(ModItemGroup.AA_MBVD_TAB)
                        .isImmuneToFire()
                        .maxStackSize(1)
        );
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> iTextComponents, ITooltipFlag flag) {
        iTextComponents.add(new TranslationTextComponent("tooltip.metal_detector").mergeStyle(TextFormatting.BOLD).mergeStyle(TextFormatting.YELLOW));
        super.addInformation(itemStack, world, iTextComponents, flag);
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        if (target.getDisplayName().getString().equals(new TranslationTextComponent("target.manfred").getString())) {
            target.setMotion(0, 0.5, 0);
            target.addPotionEffect(new EffectInstance(Effects.GLOWING, 5, 0));
            target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 25, 10));
        }
        return super.hitEntity(itemStack, target, attacker);
    }

    @Override
    public void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {
        Entity entity = tools.getEntityPlayerIsLookingAt_usable(player);
        if (this.target == null && entity != null && entity != player && entity.isAlive() && entity instanceof LivingEntity) {
            this.target = (LivingEntity) entity;
            this.targetPos = this.target.getPositionVec();
        }
    }

    @Override
    public void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {
        this.target = null;
        this.targetPos = null;
        this.hasPlayed = false;
    }

    @Override
    public void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {
        Entity entity = tools.getEntityPlayerIsLookingAt_usable(player);
        if (this.target == null && entity != null && entity != player && entity.isAlive() && entity instanceof LivingEntity) {
            this.target = (LivingEntity) entity;
            this.targetPos = this.target.getPositionVec();
        }

        if (this.target != null) {
            if (this.target.getDisplayName().getString().equals(new TranslationTextComponent("target.manfred").getString())) {
                this.target.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 114514, 10));
                this.target.addPotionEffect(new EffectInstance(Effects.WITHER, 114514, 10));
                this.target.attackEntityFrom(new DamageSource("Metal Detector"), 10F);
                this.target.setPosition(this.targetPos.x, this.targetPos.y, this.targetPos.z);
                if (!this.hasPlayed){
                    Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(ModSounds.MANFRED_HITTING_WALL.get(), 1F, 3F));
                    this.hasPlayed = true;
                }
            }
        }
    }
}
