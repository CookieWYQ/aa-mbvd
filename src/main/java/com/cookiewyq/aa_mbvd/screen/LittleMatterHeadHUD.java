// 文件: src/main/java/com/cookiewyq/cookiemod/screen/LittleMatterHeadHUD.java
package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class LittleMatterHeadHUD extends AbstractGui {
    private static final Minecraft mc = Minecraft.getInstance();

    private LittleMatter_Words word;
    private LittleMatter_Roles role;
    private LittleMatter_Langs lang;
    private final MatrixStack matrixStack;
    private ResourceLocation BG;
    private final long displayStartTime;
    private final PlayerEntity player;

    private double x, y, z;

    public LittleMatterHeadHUD(MatrixStack matrixStack, LittleMatter_Words word, LittleMatter_Roles role,
                               LittleMatter_Langs lang, long displayStartTime, double x, double y, double z) {
        this.word = word;
        this.role = role;
        this.lang = lang;
        this.matrixStack = matrixStack;
        this.displayStartTime = displayStartTime;
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = null; // 不再需要玩家实体
        setBG(lang, word);
    }

    // 在 LittleMatterHeadHUD.java 中添加新的构造函数参数
    public LittleMatterHeadHUD(MatrixStack matrixStack, LittleMatter_Words word, LittleMatter_Roles role,
                               LittleMatter_Langs lang, long displayStartTime, PlayerEntity player) {
        this.word = word;
        this.role = role;
        this.lang = lang;
        this.matrixStack = matrixStack;
        this.displayStartTime = displayStartTime;
        this.player = player;
        // 如果有玩家实体，则使用玩家的位置
        if (player != null) {
            this.x = player.getPosX();
            this.y = player.getPosY() + player.getHeight() + 0.5; // 在玩家头顶显示
            this.z = player.getPosZ();
        }
        setBG(lang, word);
    }


    public void setBG(LittleMatter_Langs lang, LittleMatter_Words word) {
        if (lang != null && word != null) {
            this.BG = new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/hud/" + lang.getId() + "_" + word.getId() + ".png");
        }
    }

    public void setRole(LittleMatter_Roles role) {
        this.role = role;
        setBG(lang, word);
    }

    public void setWord(LittleMatter_Words word) {
        this.word = word;
        setBG(lang, word);
    }

    public void setLang(LittleMatter_Langs lang) {
        this.lang = lang;
        setBG(lang, word);
    }

    public void renderHeadDisplay() {
        // 检查必要条件
        if (mc.player == null || BG == null || player == null) return;

        // 计算显示时间
        float elapsedTime = (System.currentTimeMillis() - displayStartTime) / 1000.0f;
        if (elapsedTime > 1.5f) return; // 只显示1.5秒

        // 获取渲染器管理器
        EntityRendererManager renderManager = mc.getRenderManager();

        // 获取玩家位置
        double playerX = player.getPosX();
        double playerY = player.getPosY() + player.getHeight() + 0.5; // 在玩家头顶显示
        double playerZ = player.getPosZ();

        // 创建新的矩阵栈进行变换
        MatrixStack matrixStack = new MatrixStack();

        // 转换到屏幕坐标
        float scale = 0.02f;
        matrixStack.translate(
                (float) playerX - (float) renderManager.info.getProjectedView().x,
                (float) playerY - (float) renderManager.info.getProjectedView().y,
                (float) playerZ - (float) renderManager.info.getProjectedView().z
        );

        // 执行旋转
        matrixStack.rotate(Vector3f.YP.rotationDegrees(-renderManager.info.getYaw()));
        matrixStack.rotate(Vector3f.XP.rotationDegrees(renderManager.info.getPitch()));

        // 执行缩放
        matrixStack.scale(-scale, -scale, scale);

        // 启用混合
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);

        // 计算透明度（淡入淡出效果）
        float alpha = 1.0f;
        if (elapsedTime < 0.2f) {
            alpha = elapsedTime / 0.2f; // 淡入
        } else if (elapsedTime > 1.3f) {
            alpha = (1.5f - elapsedTime) / 0.2f; // 淡出
        }

        // 应用透明度
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, alpha);

        // 绑定纹理并绘制
        mc.getTextureManager().bindTexture(BG);

        // 绘制图片（居中显示）
        int width = 150;
        int height = 140;
        blit(matrixStack, -width / 2, -height / 2, 0, 0, width, height, width, height);

        // 恢复渲染状态
        RenderSystem.disableBlend();
    }

}
