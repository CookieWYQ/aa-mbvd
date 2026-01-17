package com.cookiewyq.aa_mbvd.items.custom.badges;

import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class Prosbadge extends ModThrowableItem implements IEvidenceCallback, ICurioItem, IBadge {
    public Prosbadge() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .rarity(Rarity.RARE)
                .setNoRepair()
                .maxStackSize(1)
        );
    }

    @Override
    public void playSound(World world, PlayerEntity player) {
        LittleMatter_Words words = LittleMatter_Words.getRandomWord();
        LittleMatter_Langs lang = LittleMatter_Langs.getRandomLang();
        LittleMatter_Roles role = LittleMatter_Roles.Miles_Edgeworth;
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.getLittleMatterSound(words, role, lang).get(), SoundCategory.PLAYERS, 1F, 1F);
    }

    @Override
    public boolean getIsShiftPressedWhenThrow() {
        return true;
    }

    @Override
    public boolean isRemoveOnImpact(ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        return true;
    }

    @Override
    public void onEntityHit(EntityRayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {

        if (rayTraceResult.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) rayTraceResult.getEntity();
            livingEntity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 200, 1));
        }

    }

    @Override
    public void onImpact(RayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {

        player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 200, 4));

    }

    @Override
    public void tick(World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        modThrowableEntity.setMotion(modThrowableEntity.getMotion().mul(1.0D, random.nextFloat() + (random.nextBoolean() ? -1 : 1), 1.0D));
    }

    @Override
    public void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {

        player.sendStatusMessage(new StringTextComponent("Pros:Pre"), false);

        player.sendStatusMessage(new StringTextComponent("I'm not a bad guy, I'm a good guy."), false);

    }

    @Override
    public void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {
        player.sendStatusMessage(new StringTextComponent("Pros:Post"), false);

        player.sendStatusMessage(new StringTextComponent("Because of you, I have had more emo."), false);

    }

    @Override
    public void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {

        player.sendStatusMessage(new StringTextComponent("Pros:Showing").mergeStyle(TextFormatting.RED), true);

    }
}
