package com.cookiewyq.aa_mbvd.items.npc;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.items.ModItemGroup;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.OpenNpcEditorPacket;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.*;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * NPC 转换道具
 */

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NpcConverterItem extends Item {

    public NpcConverterItem() {
        super(new Properties()
                .rarity(Rarity.RARE)
                .maxStackSize(1)
                .group(ModItemGroup.AA_MBVD_TAB)
                .isImmuneToFire()
        );
    }

    @Override
    public ActionResultType itemInteractionForEntity(
            ItemStack stack,
            PlayerEntity player,
            LivingEntity target,
            Hand hand) {

        // 必须按住 Shift
        if (!player.isSneaking()) return ActionResultType.PASS;
        if (player.world.isRemote) return ActionResultType.SUCCESS;


        target.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
            if (cap.isNpc()) {
                Networking.INSTANCE.sendToServer(new OpenNpcEditorPacket(target.getEntityId()));
            } else {
                BooleanConsumer confirm = (result) -> {
                    if (result) {
                        cap.setNpc(true);
                        Networking.INSTANCE.sendToServer(new OpenNpcEditorPacket(target.getEntityId()));
                    }
                    if (Minecraft.getInstance().currentScreen != null) {
                        Minecraft.getInstance().currentScreen.closeScreen();
                    }
                };
                Minecraft.getInstance().displayGuiScreen(new ConfirmScreen(
                        confirm,
                        new TranslationTextComponent("gui.confirm.convert_entity_to_npc_1"),
                        new TranslationTextComponent("gui.confirm.convert_entity_to_npc_2")));
            }
        });


        return ActionResultType.SUCCESS;
    }
}
