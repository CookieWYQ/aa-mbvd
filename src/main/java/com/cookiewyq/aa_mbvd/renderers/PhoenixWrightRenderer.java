package com.cookiewyq.aa_mbvd.renderers;

import com.cookiewyq.aa_mbvd.entities.custom.PhoenixWrightEntity;
import com.cookiewyq.aa_mbvd.models.PhoenixWrightModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PhoenixWrightRenderer extends GeoEntityRenderer<PhoenixWrightEntity> {
    public PhoenixWrightRenderer(EntityRendererManager manager) {
        super(manager, new PhoenixWrightModel());
    }

    @Override
    public ResourceLocation getEntityTexture(PhoenixWrightEntity entity) {
        return PhoenixWrightModel.textureResource;
    }

    public RenderType getRenderType(PhoenixWrightEntity animatable, float partialTicks, MatrixStack stack, IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntityTranslucent(this.getTextureLocation(animatable));
    }
}