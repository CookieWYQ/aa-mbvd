package com.cookiewyq.aa_mbvd.world.structure.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class CourtStructureConfig implements IFeatureConfig {
    public static final Codec<CourtStructureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("spacing").forGetter(config -> config.spacing),
                    Codec.INT.fieldOf("separation").forGetter(config -> config.separation),
                    Codec.INT.fieldOf("salt").forGetter(config -> config.salt)
            ).apply(instance, CourtStructureConfig::new)
    );

    private final int spacing;
    private final int separation;
    private final int salt;

    public CourtStructureConfig(int spacing, int separation, int salt) {
        this.spacing = spacing;
        this.separation = separation;
        this.salt = salt;
    }

    public int getSpacing() {
        return spacing;
    }

    public int getSeparation() {
        return separation;
    }

    public int getSalt() {
        return salt;
    }
}
