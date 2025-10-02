package com.cookiewyq.aa_mbvd.network.sendPacks.showingEvidenceEvents;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.util.EffectEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class GivingEffectEventSendPack {

    Integer entity_id;
    EffectEnum effect;
    int duration;
    int amplifier;

    public GivingEffectEventSendPack(Integer entity_id, Effect effect, int duration, int amplifier) {
        this.entity_id = entity_id;
        this.effect = EffectEnum.getEffectEnumFromEffect(effect);
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public GivingEffectEventSendPack(PacketBuffer buffer) {
        this.entity_id = buffer.readInt();
        this.effect = buffer.readEnumValue(EffectEnum.class);
        this.duration = buffer.readInt();
        this.amplifier = buffer.readInt();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entity_id);
        buffer.writeEnumValue(this.effect);
        buffer.writeInt(this.duration);
        buffer.writeInt(this.amplifier);

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().world != null && !Minecraft.getInstance().world.isRemote) {
                Entity entity = Minecraft.getInstance().world.getEntityByID(this.entity_id);
                if (entity != null && entity.isAlive() && entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addPotionEffect(new net.minecraft.potion.EffectInstance(this.effect.getEffect(), this.duration, this.amplifier));
                } else {
                    if (entity != null) {
                        AA_MbvdMod.PLOGGER.error("Fuck GivingEffectEventSendPack: entity is null or world is NOT remote or entity is LivingEntity. Entity::toString: {}",  entity::toString);
                    }
                }
            } else {
                AA_MbvdMod.PLOGGER.error("Fuck GivingEffectEventSendPack: world is null");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
