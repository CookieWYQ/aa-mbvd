package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.events.HudClientEvent;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ShowingEvidenceHUD extends AbstractGui {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final ResourceLocation BG_TEX =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/hud/badge_bg.png");

    private final ItemStack stack;
    private final MatrixStack matrixStack;

    public ShowingEvidenceHUD(MatrixStack matrixStack, ItemStack itemStack) {
        this.stack = itemStack;
        this.matrixStack = matrixStack;
    }

    public void show() {
        ItemStack displayStack = stack.copy();

        if (displayStack.isEmpty()) return;

        float item_scale = 3.0F; // 缩放因子
        int bgSize = 64;

        // 整体位置：屏幕左上方，物品栏上方
        int x = mc.getMainWindow().getScaledWidth() / 2 - 145;
        int y = mc.getMainWindow().getScaledHeight() - 115;

        // 动画：透明度与缩放（整体）
        float age = (System.currentTimeMillis() - HudClientEvent.getDisplayItemStartTime()) / 1000F;
        float alpha = age < 0.2F ? age / 0.2F : 1F - (age - 0.2F) / 1.8F;
        float scale = 1F + (1F - 0.5F) * 0.5F;
        alpha = Math.max(0F, Math.min(1F, alpha));

        // 启用混合
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

        // 应用整体变换
        matrixStack.push();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, 1);

        // 1. 画灰色背景 (64x64像素)
        mc.getTextureManager().bindTexture(BG_TEX);

        blit(matrixStack, 0, 0, 0, 0, bgSize, bgSize, bgSize, bgSize);

        // 2. 画物品（居中）
        ItemRenderer ir = mc.getItemRenderer();


//        // 使用OpenGL直接操作矩阵来渲染居中的缩放物品
        RenderSystem.pushMatrix();

        // 移动到背景中心位置 (32, 32) 是 64x64 背景的中心
//        RenderSystem.translatef(bgSize / 2.0f, bgSize / 2.0f, 0.0f);

        // 应用物品缩放
        RenderSystem.scalef(item_scale, item_scale, 1.0f);

        // 调整锚点，使物品中心对齐 (因为标准物品是16x16)
//        RenderSystem.translatef((mc.getMainWindow().getScaledWidth() / 2.0f / itemScale - 16.0f) - 25f, (mc.getMainWindow().getScaledHeight() / 2.0f / itemScale - 16.0f) + 27f, 0.0f);
//        RenderSystem.translatef((mc.getMainWindow().getScaledWidth() / 2.0f / itemScale - 16.0f) - (mc.getMainWindow().getScaledWidth() / 17.08f), (mc.getMainWindow().getScaledHeight() / 2.0f / itemScale - 16.0f) + (mc.getMainWindow().getScaledHeight() / 8.888889f), 0.0f);
        RenderSystem.translatef((x + 16) / item_scale, (y + 16) / item_scale, 0);
//        if (mc.player != null) {
//            mc.player.sendChatMessage("w: " + mc.getMainWindow().getScaledWidth() / 25f + "; h: " + mc.getMainWindow().getScaledHeight() / 27f);
//        }

        // 设置透明度
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

        // 渲染物品到原点(0,0)，经过上面的变换后会出现在背景中心
        if (mc.player != null) {
            ir.renderItemAndEffectIntoGUI(mc.player, displayStack, 0, 0);
        }

        RenderSystem.popMatrix();

        matrixStack.pop();

        // 禁用混合
        RenderSystem.disableBlend();
    }
}
