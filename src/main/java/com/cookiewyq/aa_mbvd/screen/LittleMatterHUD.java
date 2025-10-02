package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Langs;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Roles;
import com.cookiewyq.aa_mbvd.util.little_matter.LittleMatter_Words;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

public class LittleMatterHUD extends AbstractGui {

    private static final Minecraft mc = Minecraft.getInstance();

    private LittleMatter_Words word;
    private LittleMatter_Roles role;
    private LittleMatter_Langs lang;
    private final MatrixStack matrixStack;
    private ResourceLocation BG;

    private final long displayStartTime;

    public LittleMatterHUD(MatrixStack matrixStack, LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang, long displayStartTime) {
        this.word = word;
        this.role = role;
        this.lang = lang;
        this.matrixStack = matrixStack;
        this.displayStartTime = displayStartTime; // 使用传入的时间
        setBG(lang, word);
    }

    public LittleMatterHUD(MatrixStack matrixStack, LittleMatter_Words word, LittleMatter_Roles role, LittleMatter_Langs lang) {
        this(matrixStack, word, role, lang, System.currentTimeMillis());
    }

    public LittleMatterHUD(MatrixStack matrixStack) {
        this(matrixStack, LittleMatter_Words.getRandomWord(), LittleMatter_Roles.getRandomRole(), LittleMatter_Langs.getRandomLang());
    }


    public void setBG(LittleMatter_Langs lang, LittleMatter_Words word) {
        if (lang != null && word != null) {
            this.BG = new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/hud/" + lang.getId() + "_" + word.getId() + ".png");
        }
    }

    public void show() {
        // 计算屏幕中心位置
        int screenWidth = mc.getMainWindow().getScaledWidth();
        int screenHeight = mc.getMainWindow().getScaledHeight();

        // HUD尺寸
        int hudWidth = 150;
        int hudHeight = 140;

        // 计算居中位置
        int x = (screenWidth - hudWidth) / 2;
        int y = (screenHeight - hudHeight) / 2;

        // 计算晃动效果 - 使用固定的起始时间，而不是每次重新创建实例时的时间
        float elapsedTime = (System.currentTimeMillis() - displayStartTime) / 1000.0f;

        // 增加初始晃动强度
        float initialShakeIntensity = 10.0f;
        float shakeDecay = 20f; // 减慢衰减速度
        float currentShakeIntensity = Math.max(0, initialShakeIntensity - elapsedTime * shakeDecay);

        // 更强烈的晃动效果

        // 更快的频率组合
        float shakeX = (float) (Math.sin(elapsedTime * 25) * 2.9f +
                Math.sin(elapsedTime * 30) * 3.5f);
        float shakeY = (float) (Math.cos(elapsedTime * 23) * 2.6f +
                Math.sin(elapsedTime * 22) * 3.3f);


        // 应用当前强度
        shakeX *= currentShakeIntensity;
        shakeY *= currentShakeIntensity;

        RenderSystem.blendColor(1F, 1F, 1F, 1F);
        RenderSystem.enableBlend();

        // 绑定纹理并绘制
        mc.getTextureManager().bindTexture(BG);

        // 在屏幕中心绘制HUD，加上晃动偏移
        blit(matrixStack, x + (int) shakeX, y + (int) shakeY, 0, 0, hudWidth, hudHeight, hudWidth, hudHeight);
    }


    public void setWord(LittleMatter_Words word) {
        this.word = word;
        setBG(lang, word);
    }

    public void setRole(LittleMatter_Roles role) {
        this.role = role;
        setBG(lang, word);
    }

    public void setLang(LittleMatter_Langs lang) {
        this.lang = lang;
        setBG(lang, word);
    }

    public LittleMatter_Words getWord() {
        return word;
    }

    public LittleMatter_Roles getRole() {
        return role;
    }

    public LittleMatter_Langs getLang() {
        return lang;
    }
}
