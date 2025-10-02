package com.cookiewyq.aa_mbvd.items.custom.other;

import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MayasMagatama extends Item implements IEvidenceCallback, ICurioItem {
    public MayasMagatama() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .rarity(Rarity.RARE)
                .maxStackSize(1)
                .isImmuneToFire()
                .setNoRepair()
        );
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {

        ICurioItem.super.curioTick(identifier, index, livingEntity, stack);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> iTextComponents, ITooltipFlag flag) {

        super.addInformation(itemStack, world, iTextComponents, flag);
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
