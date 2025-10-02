package com.cookiewyq.aa_mbvd.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;

public class hud_tools {

    /**
     * 渲染缩放后的物品（支持绝对坐标定位）
     * @param x X坐标
     * @param y Y坐标
     * @param scale 缩放因子
     * @param alpha 透明度
     * @param itemStack 待渲染的物品
     * @param mc Minecraft实例
     */
    public static void renderScaledItemWithPosition(int x, int y, float scale, float alpha, ItemStack itemStack, Minecraft mc) {
//        RenderSystem.pushMatrix();
//        matrixStack.scale(scale, scale, 1.0f);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);
        RenderSystem.pushMatrix();
        RenderSystem.scalef(scale, scale, 1.0f);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

        // 渲染物品
        ItemRenderer itemRenderer = mc.getItemRenderer();
        if (mc.player != null) {
            itemRenderer.renderItemAndEffectIntoGUI(mc.player, itemStack, x, y);
        }

//        matrixStack.pop();
        RenderSystem.popMatrix();
    }

    /**
     * 渲染缩放后的物品（仅渲染物品本身，不渲染耐久等信息）
     * @param scale 缩放因子
     * @param alpha 透明度
     * @param itemStack 待渲染的物品
     * @param mc Minecraft实例
     */
    public static void renderScaledItem(float scale, float alpha, ItemStack itemStack, Minecraft mc) {
        // 保存当前渲染状态
        RenderSystem.pushMatrix();
        RenderSystem.scalef(scale, scale, 1.0f);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, alpha);

        // 渲染物品（仅渲染物品本身，不渲染overlay）
        ItemRenderer itemRenderer = mc.getItemRenderer();
        if (mc.player != null) {
            itemRenderer.renderItemAndEffectIntoGUI(mc.player, itemStack, -8, -8);
        }

        // 恢复渲染状态
        RenderSystem.popMatrix();
    }
}
