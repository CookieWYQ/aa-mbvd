// 文件: src/main/java/com/cookiewyq/cookiemod/screen/LittleMatterDisplayManager.java
// 文件: src/main/java/com/cookiewyq/cookiemod/screen/LittleMatterDisplayManager.java
package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LittleMatterDisplayManager {
    private static final Map<UUID, DisplayRequest> displayRequests = new ConcurrentHashMap<>();

    public static class DisplayRequest {
        public final LittleMatter_Words word;
        public final LittleMatter_Roles role;
        public final LittleMatter_Langs lang;
        public final Vector3d position;
        public final long startTime;

        public DisplayRequest(LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang, Vector3d position, long startTime) {
            this.word = word;
            this.role = role;
            this.lang = lang;
            this.position = position;
            this.startTime = startTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - startTime > 1500; // 1.5秒后过期
        }
    }

    public static void addDisplayRequest(UUID playerUUID, LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang, Vector3d position, long startTime) {
        displayRequests.put(playerUUID, new DisplayRequest(word, role, lang, position, startTime));
    }

    public static void renderDisplays() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.world == null) return;

        // 清除过期的请求
        Iterator<Map.Entry<UUID, DisplayRequest>> iterator = displayRequests.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, DisplayRequest> entry = iterator.next();
            if (entry.getValue().isExpired()) {
                iterator.remove();
                continue;
            }

            // 查找对应的玩家
            PlayerEntity player = null;
            for (PlayerEntity p : mc.world.getPlayers()) {
                if (p.getUniqueID().equals(entry.getKey())) {
                    player = p;
                    break;
                }
            }

            // 渲染头顶图片（包括自己的）
            if (player != null) {
                DisplayRequest request = entry.getValue();
                LittleMatterHeadHUD hud = new LittleMatterHeadHUD(
                        new com.mojang.blaze3d.matrix.MatrixStack(),
                        request.word,
                        request.role,
                        request.lang,
                        request.startTime,
                        player
                );
                hud.renderHeadDisplay();
            }
        }
    }

    public static void clearAll() {
        displayRequests.clear();
    }
}
