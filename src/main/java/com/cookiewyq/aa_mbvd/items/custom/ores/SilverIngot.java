package com.cookiewyq.aa_mbvd.items.custom.ores;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.screen.NPC_DialogScreen;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SilverIngot extends Item {
    public SilverIngot() {
        super(new Properties()
                .group(ModItemGroup.AA_MBVD_TAB)
                .maxStackSize(64)
                .rarity(Rarity.UNCOMMON)
        );
    }

    public ActionResultType itemInteractionForEntity(
            ItemStack stack,
            PlayerEntity player,
            LivingEntity target,
            Hand hand) {

        if (target.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).isPresent()) {
            target.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                if (cap.isNpc()){
                    Minecraft.getInstance().displayGuiScreen(NPC_DialogScreen.INSTANCE);
                    NPC_DialogScreen.INSTANCE.setEntityId(target.getEntityId());
                }
            });
        }

        return ActionResultType.PASS;

    }
}
