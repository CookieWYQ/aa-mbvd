package com.cookiewyq.aa_mbvd.particles;

import com.mojang.serialization.Codec;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ObjectionParticleType extends ParticleType<ObjectionParticleData> {
    public ObjectionParticleType() {
        super(false, ObjectionParticleData.DESERIALIZER);
    }

    @Override
    public Codec<ObjectionParticleData> func_230522_e_() {
        return Codec.unit(new ObjectionParticleData(new Vector3d(0, 0, 0), new Color(0), 0));
    }
}
