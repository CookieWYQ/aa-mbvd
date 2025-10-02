package com.cookiewyq.aa_mbvd.keyBinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;


public class ModKeyBindings {

    public static final KeyBinding Show_Badge__Key = new KeyBinding(
            "key.aa_mbvd.show_badge",   // 语言文件里的键名
            GLFW.GLFW_KEY_V,           // 默认按 V
            "key.categories.aa_mbvd");

    public static final KeyBinding Objection__Key = new KeyBinding(
            "key.aa_mbvd.objection",   // 语言文件里的键名
            GLFW.GLFW_KEY_O,           // 默认按 O
            "key.categories.aa_mbvd");

    public static final KeyBinding Holdit__Key = new KeyBinding(
            "key.aa_mbvd.holdit",   // 语言文件里的键名
            GLFW.GLFW_KEY_H,           // 默认按 H
            "key.categories.aa_mbvd");

    public static final KeyBinding Takethat__Key = new KeyBinding(
            "key.aa_mbvd.takethat",   // 语言文件里的键名
            GLFW.GLFW_KEY_TAB,           // 默认按 Tab
            "key.categories.aa_mbvd");

    public static final KeyBinding Show_Photo__Key = new KeyBinding(
            "key.aa_mbvd.show_photo",   // 语言文件里的键名
            GLFW.GLFW_KEY_N,           // 默认按 N
            "key.categories.aa_mbvd");

    public static final KeyBinding Open_Court_Records__Key = new KeyBinding(
            "key.aa_mbvd.open_court_records",   // 语言文件里的键名
            GLFW.GLFW_KEY_R,           // 默认按 R
            "key.categories.aa_mbvd");

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Show_Badge__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Objection__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Holdit__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Takethat__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Show_Photo__Key));
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(Open_Court_Records__Key));
    }
}
