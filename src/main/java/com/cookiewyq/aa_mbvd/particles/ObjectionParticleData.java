package com.cookiewyq.aa_mbvd.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.util.Locale;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ObjectionParticleData implements IParticleData {
    private final Vector3d speed;
    private final Color color;
    private final float diameter;
    public static final IDeserializer<ObjectionParticleData> DESERIALIZER = new IDeserializer<ObjectionParticleData>() {

        @Override
        public ObjectionParticleData deserialize(ParticleType<ObjectionParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            final int MIN_COLOUR = 0;
            final int MAX_COLOUR = 255;
            reader.expect(' ');
            double speedX = reader.readDouble();
            reader.expect(' ');
            double speedY = reader.readDouble();
            reader.expect(' ');
            double speedZ = reader.readDouble();
            reader.expect(' ');
            int red = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
            reader.expect(' ');
            int green = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
            reader.expect(' ');
            int blue = MathHelper.clamp(reader.readInt(), MIN_COLOUR, MAX_COLOUR);
            reader.expect(' ');
            int alpha = MathHelper.clamp(reader.readInt(), 1, MAX_COLOUR);
            reader.expect(' ');
            float diameter = reader.readFloat();
            return new ObjectionParticleData(new Vector3d(speedX, speedY, speedZ), new Color(red, green, blue, alpha), diameter);
        }

        @Override
        public ObjectionParticleData read(ParticleType<ObjectionParticleData> particleTypeIn, PacketBuffer buffer) {
            final int MIN_COLOUR = 0;
            final int MAX_COLOUR = 255;
            double speedX = buffer.readDouble();
            double speedY = buffer.readDouble();
            double speedZ = buffer.readDouble();
            int red = MathHelper.clamp(buffer.readInt(), MIN_COLOUR, MAX_COLOUR);
            int green = MathHelper.clamp(buffer.readInt(), MIN_COLOUR, MAX_COLOUR);
            int blue = MathHelper.clamp(buffer.readInt(), MIN_COLOUR, MAX_COLOUR);
            int alpha = MathHelper.clamp(buffer.readInt(), 1, MAX_COLOUR);
            float diameter = buffer.readFloat();
            return new ObjectionParticleData(new Vector3d(speedX, speedY, speedZ), new Color(red, green, blue, alpha), diameter);
        }
    };

    public ObjectionParticleData(Vector3d speed, Color color, float diameter) {
        this.speed = speed;
        this.color = color;
        this.diameter = diameter;
        System.out.println("Creating ObjectionParticleData with speed: " + speed + ", color: " + color + ", diameter: " + diameter);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.objectionParticle.get();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeDouble(this.speed.x);
        buffer.writeDouble(this.speed.y);
        buffer.writeDouble(this.speed.z);
        buffer.writeInt(this.color.getRed());
        buffer.writeInt(this.color.getGreen());
        buffer.writeInt(this.color.getBlue());
        buffer.writeInt(this.color.getAlpha());
        buffer.writeFloat(this.diameter);
    }

    @Override
    public String getParameters() {
        return String.format(Locale.ROOT, "%s %.2f %d %d %d %d %.2f %.2f %.2f",
                this.getType().getRegistryName(), this.diameter, this.color.getRed(), this.color.getGreen(), this.color.getBlue(), this.color.getAlpha(), this.speed.getX(), this.speed.getY(), this.speed.getZ());
    }

    public Vector3d getSpeed() {
        return speed;
    }

    public Color getColor() {
        return color;
    }

    public float getDiameter() {
        return diameter;
    }
}