package com.cookiewyq.aa_mbvd.items.evidence;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IEvidenceCallback {
    default void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {
    }

    default void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {
    }

    default void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {
    }
}