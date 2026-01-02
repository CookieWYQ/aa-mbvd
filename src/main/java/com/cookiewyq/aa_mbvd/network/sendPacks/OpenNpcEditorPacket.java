package com.cookiewyq.aa_mbvd.network.sendPacks;

import com.cookiewyq.aa_mbvd.container.NpcEditorContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class OpenNpcEditorPacket {

//    private final UUID npcUUID;
    private final int npcID;

    public OpenNpcEditorPacket(int npcID) {
//        this.npcUUID = npcUUID;
        this.npcID = npcID;
    }

    public OpenNpcEditorPacket(PacketBuffer buffer) {
//        this.npcUUID = buffer.readUniqueId();
        this.npcID = buffer.readInt();
    }

    public void encode(PacketBuffer buffer) {
//        buffer.writeUniqueId(npcUUID);
        buffer.writeInt(npcID);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity p = ctx.get().getSender();
            if (p != null) {
                Entity npcEntity = p.getEntityWorld().getEntityByID(npcID);
                if (npcEntity instanceof MobEntity) {
                    NetworkHooks.openGui(p, new INamedContainerProvider() {
                        @Nonnull
                        @Override
                        public ITextComponent getDisplayName() {
                            return new TranslationTextComponent("container.aa_mbvd.npc_editor");
                        }

                        @Nonnull
                        @Override
                        public Container createMenu(int i, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
                            return new NpcEditorContainer(i, playerInventory, (MobEntity) npcEntity);
                        }
                    });
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}