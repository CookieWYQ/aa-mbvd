
package com.cookiewyq.aa_mbvd.network;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.network.sendPacks.*;
import com.cookiewyq.aa_mbvd.network.sendPacks.showingEvidenceEvents.GivingEffectEventSendPack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class Networking {
    public static SimpleChannel INSTANCE;
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }

    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(AA_MbvdMod.MOD_ID, "mbvd"),
                () -> VERSION,
                (version) -> version.equals(VERSION),
                (version) -> version.equals(VERSION)
        );

        INSTANCE.messageBuilder(LittleMatterSendPacket.class, nextID())
                .encoder(LittleMatterSendPacket::toBytes)
                .decoder(LittleMatterSendPacket::new)
                .consumer(LittleMatterSendPacket::handle)
                .add();

        INSTANCE.messageBuilder(LittleMatterHeadDisplayPacket.class, nextID())
                .encoder(LittleMatterHeadDisplayPacket::toBytes)
                .decoder(LittleMatterHeadDisplayPacket::new)
                .consumer(LittleMatterHeadDisplayPacket::handle)
                .add();

        INSTANCE.messageBuilder(ShowingEvidenceSendPacket.class, nextID())
                .encoder(ShowingEvidenceSendPacket::toBytes)
                .decoder(ShowingEvidenceSendPacket::new)
                .consumer(ShowingEvidenceSendPacket::handle)
                .add();

        INSTANCE.messageBuilder(GivingEffectEventSendPack.class, nextID())
                .encoder(GivingEffectEventSendPack::toBytes)
                .decoder(GivingEffectEventSendPack::new)
                .consumer(GivingEffectEventSendPack::handle)
                .add();

        INSTANCE.messageBuilder(OpenCourtRecordScreenPacket.class, nextID())
                .encoder(OpenCourtRecordScreenPacket::encode)
                .decoder(OpenCourtRecordScreenPacket::new)
                .consumer(OpenCourtRecordScreenPacket::handle)
                .add();

        INSTANCE.messageBuilder(UpdateCourtRecordDataSendPack.class, nextID())
                .encoder(UpdateCourtRecordDataSendPack::toBytes)
                .decoder(UpdateCourtRecordDataSendPack::new)
                .consumer(UpdateCourtRecordDataSendPack::handle)
                .add();

        INSTANCE.messageBuilder(GetServerCourtRecordDataSendPack.class, nextID())
                .encoder(GetServerCourtRecordDataSendPack::toBytes)
                .decoder(GetServerCourtRecordDataSendPack::new)
                .consumer(GetServerCourtRecordDataSendPack::handle)
                .add();

        INSTANCE.messageBuilder(GetClientCourtRecordDataSendPack.class, nextID())
                .encoder(GetClientCourtRecordDataSendPack::toBytes)
                .decoder(GetClientCourtRecordDataSendPack::new)
                .consumer(GetClientCourtRecordDataSendPack::handle)
                .add();

    }
}
