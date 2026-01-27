package com.cookiewyq.aa_mbvd.particles;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particles.BasicParticleType;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ObjectionParticleFactory  implements IParticleFactory<ObjectionParticleData> {
    private final IAnimatedSprite sprites;

    public ObjectionParticleFactory(IAnimatedSprite sprite) {
        this.sprites = sprite;
        System.out.println("ObjectionParticleFactory instantiated with sprite");
    }

    @Override
    public Particle makeParticle(ObjectionParticleData typeIn, ClientWorld worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        System.out.println("Creating ObjectionParticle via factory at: " + x + ", " + y + ", " + z);
        ObjectionParticle particle = new ObjectionParticle(worldIn, x, y, z, typeIn.getSpeed(), typeIn.getColor(), typeIn.getDiameter());
        particle.selectSpriteRandomly(this.sprites);
        System.out.println("ObjectionParticle created successfully");
        return particle;
    }
}