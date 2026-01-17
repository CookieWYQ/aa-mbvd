package com.cookiewyq.aa_mbvd.items.custom.badges;

import com.cookiewyq.aa_mbvd.entities.custom.ModThrowableEntity;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import com.cookiewyq.aa_mbvd.util.tools;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AttorneysBadge extends ModThrowableItem implements IEvidenceCallback, ICurioItem, IBadge {

    private LivingEntity showingGettingEntity;

    public AttorneysBadge() {
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
        LittleMatter_Roles role = LittleMatter_Roles.Phoenix_Wright;
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), ModSounds.getLittleMatterSound(words, role, lang).get(), SoundCategory.PLAYERS, 1F, 1F);
    }

    @Override
    public boolean isRemoveOnImpact(ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        return modThrowableEntity.ticksExisted <= 5000;
    }

    @Override
    public void onEntityHit(EntityRayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        Entity entity = rayTraceResult.getEntity();
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.WITHER, 10, 1));
        }
    }

    @Override
    public boolean getIsShiftPressedWhenThrow() {
        return true;
    }

    @Override
    public void onImpact(RayTraceResult rayTraceResult, World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {
        if (!world.isRemote) {
            world.createExplosion(modThrowableEntity, modThrowableEntity.getPosX(), modThrowableEntity.getPosY(), modThrowableEntity.getPosZ(), 3.0F, false, Explosion.Mode.DESTROY);
        }
    }

    @Override
    public void tick(World world, PlayerEntity player, ItemStack itemStack, ModThrowableEntity modThrowableEntity) {

        super.tick(world, player, itemStack, modThrowableEntity);
    }


    @Override
    public void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {
//        player.sendStatusMessage(new StringTextComponent("Att:Pre"), false);
        Entity targetEntity = tools.getEntityPlayerIsLookingAt_usable(player);
        if (this.showingGettingEntity == null && targetEntity != null && targetEntity != player && targetEntity.isAlive() && targetEntity instanceof LivingEntity) {
            this.showingGettingEntity = (LivingEntity) targetEntity;
        }
    }

    @Override
    public void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {

        player.addPotionEffect(new EffectInstance(Effects.SPEED, 50, 2));
//        player.sendStatusMessage(new StringTextComponent("Att:Post"), false);

        this.showingGettingEntity = null;


//        BlockRayTraceResult ray = (BlockRayTraceResult) player.pick(2, 0.0f, false);
//        BlockPos targetPos = ray.getPos();
//        player.world.getEntitiesWithinAABB(LivingEntity.class, AxisAlignedBB.fromVector(Vector3d.copyCentered(targetPos))).forEach(entity -> {
//            if (entity instanceof LivingEntity && entity != player) {
//                entity.addPotionEffect(new EffectInstance(Effects.GLOWING, 50, 1));
//                entity.sendMessage(new StringTextComponent("E:POST").mergeStyle(TextFormatting.WHITE), player.getUniqueID());
//            }
//        });

    }

    @Override
    public void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {
//        if (displaying_duration % 20 == 0)
//            player.sendStatusMessage(new StringTextComponent("Att:Showing").mergeStyle(TextFormatting.BLUE), false);

        Entity targetEntity = tools.getEntityPlayerIsLookingAt_usable(player);
        if (this.showingGettingEntity == null && targetEntity != null && targetEntity != player && targetEntity.isAlive() && targetEntity instanceof LivingEntity) {
            this.showingGettingEntity = (LivingEntity) targetEntity;
        }

        if (this.showingGettingEntity != null) {
            this.showingGettingEntity.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 114, 5));
            this.showingGettingEntity.setMotion(0, 3, 0);
        }

    }

}
