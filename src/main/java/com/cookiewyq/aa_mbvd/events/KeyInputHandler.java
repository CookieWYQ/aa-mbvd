package com.cookiewyq.aa_mbvd.events;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.keyBinding.ModKeyBindings;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.LittleMatterSendPacket;
import com.cookiewyq.aa_mbvd.network.sendPacks.OpenCourtRecordScreenPacket;
import com.cookiewyq.aa_mbvd.screen.CourtRecordScreen;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = AA_MbvdMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class KeyInputHandler {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ModKeyBindings.Open_Court_Records__Key.getKey().getKeyCode() == event.getKey() && event.getAction() == GLFW.GLFW_PRESS) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            
            if (player != null) {
                if (mc.currentScreen == null) {
                    // 请求打开法庭记录界面
                    mc.getSoundHandler().play(SimpleSound.master(ModSounds.OPEN_DETAILS.get(), 1F, 3F));
                    Networking.INSTANCE.sendToServer(new OpenCourtRecordScreenPacket());
                } else if (mc.currentScreen instanceof CourtRecordScreen) {
                    // 关闭法庭记录界面
                    mc.getSoundHandler().play(SimpleSound.master(ModSounds.CLOSE_DETAILS.get(), 1F, 3F));
                    mc.currentScreen.closeScreen();
                }
            }
        }
    }
}
