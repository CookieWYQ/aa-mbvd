package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.container.CourtRecordContainer;
import com.cookiewyq.aa_mbvd.sound.ModSounds;
import com.cookiewyq.aa_mbvd.util.tools;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class OpenCourtRecordPacket {
    private final boolean isAttorneysBadge; // true: 物品a, false: 物品b

    public OpenCourtRecordPacket(boolean isAttorneysBadge) {
        System.out.println("[DEBUG] Creating OpenCourtRecordPacket with isAttorneysBadge: " + isAttorneysBadge);
        this.isAttorneysBadge = isAttorneysBadge;
    }

    public OpenCourtRecordPacket(PacketBuffer buffer) {
        this.isAttorneysBadge = buffer.readBoolean();
        System.out.println("[DEBUG] Decoding OpenCourtRecordPacket with isAttorneysBadge: " + isAttorneysBadge);
    }

    public void encode(PacketBuffer buffer) {
        System.out.println("[DEBUG] Encoding OpenCourtRecordPacket with isAttorneysBadge: " + isAttorneysBadge);
        buffer.writeBoolean(isAttorneysBadge);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity p = ctx.get().getSender();
            if (p != null) {
                p.getCapability(Capabilities.COURT_RECORD_CAPABILITY).ifPresent(cap -> cap.setIsAttorneysBadge(isAttorneysBadge));
                NetworkHooks.openGui(p, new INamedContainerProvider() {
                    @Nonnull
                    @Override
                    public ITextComponent getDisplayName() {
                        return isAttorneysBadge ? new TranslationTextComponent("container.aa_mbvd.court_record.attorneys_badge") :
                                new TranslationTextComponent("container.aa_mbvd.court_record.prosbadge");
                    }

                    @Nonnull
                    @Override
                    public Container createMenu(int i, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
                        return new CourtRecordContainer(i, playerInventory);
                    }
                });
                p.world.playSound(null, p.getPosition(), ModSounds.OPEN_DETAILS.get(), p.getSoundCategory(), 3F, 1F);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}