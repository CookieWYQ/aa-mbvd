package com.cookiewyq.aa_mbvd.items.custom.other;

import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.custom.badges.ModThrowableItem;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class GantBoom extends ModThrowableItem implements IEvidenceCallback {
    public GantBoom() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .rarity(Rarity.EPIC)
                .maxStackSize(7)
                .isImmuneToFire()
                .setNoRepair()
        );
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> iTextComponents, ITooltipFlag flag) {
        iTextComponents.add(new TranslationTextComponent("tooltip.gant_boom").mergeStyle(TextFormatting.BOLD).mergeStyle(TextFormatting.YELLOW));
        super.addInformation(itemStack, world, iTextComponents, flag);
    }

    @Override
    public void tick(World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        if (modThrowableEntity.ticksExisted < 20) return;
        world.createExplosion(modThrowableEntity, modThrowableEntity.getPosX(), modThrowableEntity.getPosY(), modThrowableEntity.getPosZ(), 3.0F, false, Explosion.Mode.DESTROY);
        super.tick(world, player, itemStack, modThrowableEntity);
    }

    @Override
    public void playSound(World world, PlayerEntity player) {
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.GANT_BOOM_3000.get(), SoundCategory.PLAYERS, 1F, 1F);
    }

    @Override
    public int getCoolDownTicks(ItemStack itemStack, PlayerEntity player) {
        return 140;
    }

    @Override
    public boolean isRemoveOnImpact(ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        return true;
    }

    @Override
    public void onEntityHit(EntityRayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {

    }

    @Override
    public void onImpact(RayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        if (!world.isRemote) {
            world.createExplosion(modThrowableEntity, modThrowableEntity.getPosX(), modThrowableEntity.getPosY(), modThrowableEntity.getPosZ(), 5.0F, false, Explosion.Mode.DESTROY);
        }
    }

    @Override
    public void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {

        IEvidenceCallback.super.onPreShowEvidence(itemStack, player);
    }

    @Override
    public void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {

        IEvidenceCallback.super.onPostShowEvidence(itemStack, player);
    }

    @Override
    public void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {

        IEvidenceCallback.super.onShowingEvidence(itemStack, player, displaying_duration);
    }
}
