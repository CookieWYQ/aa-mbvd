package com.cookiewyq.aa_mbvd.items.custom.other;

import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.items.evidence.IEvidenceCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

public class QiansHandPrintCloth extends Item implements IEvidenceCallback {
    public QiansHandPrintCloth() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .rarity(Rarity.RARE)
                .maxStackSize(1)
                .setNoRepair()
        );
    }

    @Override
    public void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {

    }

    @Override
    public void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {

    }

    @Override
    public void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {

    }
}
