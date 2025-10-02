package com.cookiewyq.aa_mbvd.util;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum EffectEnum {
    SPEED("speed", Effects.SPEED),
    SLOWNESS("slowness", Effects.SLOWNESS),
    HASTE("haste", Effects.HASTE),
    MINING_FATIGUE("mining_fatigue", Effects.MINING_FATIGUE),
    STRENGTH("strength", Effects.STRENGTH),
    INSTANT_HEALTH("instant_health", Effects.INSTANT_HEALTH),
    INSTANT_DAMAGE("instant_damage", Effects.INSTANT_DAMAGE),
    JUMP_BOOST("jump_boost", Effects.JUMP_BOOST),
    NAUSEA("nausea", Effects.NAUSEA),
    REGENERATION("regeneration", Effects.REGENERATION),
    RESISTANCE("resistance", Effects.RESISTANCE),
    FIRE_RESISTANCE("fire_resistance", Effects.FIRE_RESISTANCE),
    WATER_BREATHING("water_breathing", Effects.WATER_BREATHING),
    INVISIBILITY("invisibility", Effects.INVISIBILITY),
    BLINDNESS("blindness", Effects.BLINDNESS),
    NIGHT_VISION("night_vision", Effects.NIGHT_VISION),
    HUNGER("hunger", Effects.HUNGER),
    WEAKNESS("weakness", Effects.WEAKNESS),
    POISON("poison", Effects.POISON),
    WITHER("wither", Effects.WITHER),
    HEALTH_BOOST("health_boost", Effects.HEALTH_BOOST),
    ABSORPTION("absorption", Effects.ABSORPTION),
    SATURATION("saturation", Effects.SATURATION),
    GLOWING("glowing", Effects.GLOWING),
    LEVITATION("levitation", Effects.LEVITATION),
    LUCK("luck", Effects.LUCK),
    UNLUCK("unluck", Effects.UNLUCK),
    SLOW_FALLING("slow_falling", Effects.SLOW_FALLING),
    CONDUIT_POWER("conduit_power", Effects.CONDUIT_POWER),
    DOLPHINS_GRACE("dolphins_grace", Effects.DOLPHINS_GRACE),
    BAD_OMEN("bad_omen", Effects.BAD_OMEN),
    HERO_OF_THE_VILLAGE("hero_of_the_village", Effects.HERO_OF_THE_VILLAGE);

    private final String name;
    private final Effect effect;

    private static final Map<String, EffectEnum> NAME_TO_EFFECT_MAP = new HashMap<>();

    static {
        for (EffectEnum effectEnum : values()) {
            NAME_TO_EFFECT_MAP.put(effectEnum.name, effectEnum);
        }
    }

    EffectEnum(String name, Effect effect) {
        this.name = name;
        this.effect = effect;
    }

    @Nullable
    public static EffectEnum getEffectEnumFromEffect(Effect effect) {
        for (EffectEnum effectEnum : values()) {
            if (effectEnum.getEffect() == effect) {
                return effectEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Effect getEffect() {
        return effect;
    }

    public static EffectEnum getByName(String name) {
        return NAME_TO_EFFECT_MAP.get(name);
    }

    public static Effect getEffectByName(String name) {
        EffectEnum effectEnum = getByName(name);
        return effectEnum != null ? effectEnum.getEffect() : null;
    }

    public static Iterable<EffectEnum> getAllEffectMaps() {
        return Arrays.asList(values());
    }
}
