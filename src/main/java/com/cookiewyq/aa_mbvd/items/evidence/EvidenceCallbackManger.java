package com.cookiewyq.aa_mbvd.items.evidence;

import com.cookiewyq.aa_mbvd.util.MapUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class EvidenceCallbackManger {
    private static final HashMap<Item, EvidenceCallback> callbacks = new HashMap<>();

    public static void registerCallback(Item item, EvidenceCallback callback) {
        callbacks.put(item, callback);
    }

    public static EvidenceCallback getCallback(Item item) {
        return callbacks.get(item);
    }

    public static HashMap<Item, EvidenceCallback> getCallbacks() {
        return MapUtils.deepCopyHashMap(callbacks);
    }

    public static void clearCallback(Item item) {
        callbacks.remove(item);
    }

    public static void clear() {
        callbacks.clear();
    }

    public static boolean isCallback(Item item) {
        return callbacks.containsKey(item);
    }

    public static void onPreShowEvidence(ItemStack itemStack, PlayerEntity player) {
        EvidenceCallback callback = getCallback(itemStack.getItem());
        if (callback != null) {
            callback.onPreShowEvidence(itemStack, player);
        }
    }

    public static void onPostShowEvidence(ItemStack itemStack, PlayerEntity player) {
        EvidenceCallback callback = getCallback(itemStack.getItem());
        if (callback != null) {
            callback.onPostShowEvidence(itemStack, player);
        }
    }

    public static void onShowingEvidence(ItemStack itemStack, PlayerEntity player, long displaying_duration) {
        EvidenceCallback callback = getCallback(itemStack.getItem());
        if (callback != null) {
            callback.onShowingEvidence(itemStack, player, displaying_duration);
        }
    }
}
