package com.cookiewyq.aa_mbvd.particles;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ObjectionParticle  extends SpriteTexturedParticle {
    protected ObjectionParticle(ClientWorld world, double x, double y, double z, Vector3d speed, Color color, float diameter) {
        super(world, x, y, z, speed.x, speed.y, speed.z);
        
        System.out.println("ObjectionParticle constructor called with params: pos=(" + x + "," + y + "," + z + "), speed=(" + speed + "), color=" + color + ", diameter=" + diameter);
        
        // 设置粒子属性
        this.maxAge = 40; // 增加生命周期，让粒子持续更长时间
        this.motionX = speed.x;
        this.motionY = speed.y;
        this.motionZ = speed.z;
        
        // 设置颜色 - 使用更简单的方式
        this.setColor(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F);
        this.setAlphaF(Math.max(0.1F, color.getAlpha() / 255F)); // 确保最小透明度为0.1
        
        // 设置粒子大小
        final float PARTICLE_SCALE_FOR_ONE_METRE = 4.0F; // 增加粒子大小使其更明显
        this.particleScale = PARTICLE_SCALE_FOR_ONE_METRE * diameter;
        
        // 确保粒子可见
        this.canCollide = false; // 禁用碰撞，这样粒子不会因碰撞而消失
        
        System.out.println("ObjectionParticle initialized with scale: " + this.particleScale);
    }

    @Override
    public IParticleRenderType getRenderType() {
        System.out.println("ObjectionParticle getRenderType called, returning PARTICLE_SHEET_OPAQUE");
        return IParticleRenderType.PARTICLE_SHEET_OPAQUE; // 使用不透明材质，因为我们使用的是不透明纹理
    }
    
    @Override
    public void tick() {
        super.tick();
        // 添加一些粒子行为
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        // 轻微的随机运动
        this.motionY += 0.001;
    }
    
    @Override
    public int getBrightnessForRender(float p_189214_1_) {
        // 确保粒子足够亮以便可见
        return 0xF000F0; // 最大亮度
    }
}