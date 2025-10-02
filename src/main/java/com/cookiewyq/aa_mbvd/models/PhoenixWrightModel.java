package com.cookiewyq.aa_mbvd.models;


import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.entities.custom.PhoenixWrightEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class PhoenixWrightModel extends AnimatedTickingGeoModel<PhoenixWrightEntity> {
    private static final ResourceLocation modelResource = new ResourceLocation(AA_MbvdMod.MOD_ID, "geo/phoenix_wright.geo.json");
    public static final ResourceLocation textureResource = new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/entity/phoenix_wright.png");
    private static final ResourceLocation animationResource = new ResourceLocation(AA_MbvdMod.MOD_ID, "animations/phoenix_wright.animation.json");

    @Override
    public ResourceLocation getModelLocation(PhoenixWrightEntity object) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureLocation(PhoenixWrightEntity object) {
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(PhoenixWrightEntity object) {
        return animationResource;
    }
}