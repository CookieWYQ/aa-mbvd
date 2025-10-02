package com.cookiewyq.aa_mbvd.items;

import com.cookiewyq.aa_mbvd.items.evidence.EvidenceCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

public class MinecraftEvidenceCallbacks {
    public static class AppleCallback extends EvidenceCallback {
        @Override
        public void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {

            player.sendStatusMessage(new StringTextComponent("Apple:Pre"), false);

        }

        @Override
        public void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {

            player.sendStatusMessage(new StringTextComponent("Apple:Post"), false);

        }

        @Override
        public void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {

            player.sendStatusMessage(new StringTextComponent("Apple:Showing"), true);

        }
    }
}
