package com.cookiewyq.aa_mbvd.sound;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Tools;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AA_MbvdMod.MOD_ID);

    // 存储批量注册的声音对象的映射
    private static final Map<String, RegistryObject<SoundEvent>> LITTLE_MATTER_SOUNDS = new HashMap<>();

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

    private static RegistryObject<SoundEvent> register(String key) {
        return SOUNDS.register(key,
                () -> new SoundEvent(new ResourceLocation(AA_MbvdMod.MOD_ID, key)));
    }

    public static final RegistryObject<SoundEvent> SHOW_BADGE = register("show_badge");
    public static final RegistryObject<SoundEvent> MANFRED_HITTING_WALL = register("manfred_hitting_wall");
    public static final RegistryObject<SoundEvent> GANT_BOOM_3000 = register("gant_boom_3000");
    public static final RegistryObject<SoundEvent> OPEN_DETAILS = register("open_details");
    public static final RegistryObject<SoundEvent> CLOSE_DETAILS = register("close_details");
    public static final RegistryObject<SoundEvent> GODOT_COFFEE_IS_A_DARK_FRAGRANCE = register("godot_coffee_is_a_dark_fragrance");

    // 批量注册并存储到映射中
    public static void registerLittleMatterSounds() {
        for (LittleMatter_Roles role : LittleMatter_Roles.values()) {
            for (LittleMatter_Words word : LittleMatter_Words.values()) {
                for (LittleMatter_Langs lang : LittleMatter_Langs.values()) {
                    if (role == LittleMatter_Roles.None || word == LittleMatter_Words.None || lang == LittleMatter_Langs.None) {
                        continue;
                    }
                    String soundKey = LittleMatter_Tools.getSoundString(word, role, lang);
                    LITTLE_MATTER_SOUNDS.put(soundKey, register(soundKey));
                }
            }
        }
    }

    public static RegistryObject<SoundEvent> getLittleMatterSound(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang) {
        String soundKey = LittleMatter_Tools.getSoundString(word, role, lang);
        return LITTLE_MATTER_SOUNDS.get(soundKey);
    }

    // 提供获取特定小事情声音的重载方法（如果 LittleMatter_Tools 有其他组合方式）
    public static RegistryObject<SoundEvent> getLittleMatterSound(String soundKey) {
        return LITTLE_MATTER_SOUNDS.get(soundKey);
    }

    // 获取所有小事情声音的映射（只读）
    public static Map<String, RegistryObject<SoundEvent>> getAllLittleMatterSounds() {
        return new HashMap<>(LITTLE_MATTER_SOUNDS); // 返回副本以防止外部修改
    }
}
