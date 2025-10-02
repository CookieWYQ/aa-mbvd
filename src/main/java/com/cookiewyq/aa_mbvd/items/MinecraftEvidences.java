package com.cookiewyq.aa_mbvd.items;

import com.cookiewyq.aa_mbvd.items.evidence.EvidenceCallbackManger;
import net.minecraft.item.Items;

public class MinecraftEvidences {
    public static void register() {
        EvidenceCallbackManger.registerCallback(Items.APPLE, new MinecraftEvidenceCallbacks.AppleCallback());
    }
}
