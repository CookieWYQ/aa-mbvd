package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.events.HudClientEvent;
import com.cookiewyq.aa_mbvd.util.hud_tools;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

public class RotatingItemHUD extends AbstractGui {
    private static final Minecraft mc = Minecraft.getInstance();

    private final ItemStack itemStack;
    private final MatrixStack matrixStack;
    private final long startTime;
    private float spendTime = 1.2f;
    private long elapsed;
    private float progress;
    private float rotation;
    private float scale;
    private float alpha;

    public RotatingItemHUD(MatrixStack matrixStack, ItemStack itemStack) {
        this.matrixStack = matrixStack;
        this.itemStack = itemStack.copy();
        this.startTime = System.currentTimeMillis();
        PLOGGER.info("创建 RotatingItemHUD，物品: {}", itemStack.getItem().getRegistryName());
    }

    public void show() {
        if (itemStack.isEmpty()) {
            PLOGGER.debug("物品为空，不显示HUD");
            return;
        }


        // 获取屏幕中心位置
        int screenWidth = mc.getMainWindow().getScaledWidth();
        int screenHeight = mc.getMainWindow().getScaledHeight();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // 计算动画参数

        this.spendTime = 1200f;
        float new_rotation = rotation, new_scale = scale, new_alpha = alpha;
        if (!Minecraft.getInstance().isGamePaused()) {
            elapsed = HudClientEvent.getEffectiveTime() - startTime;
            progress = Math.min(1.0f, elapsed / spendTime); // 0到1之间的进度值，1.2秒完成

            // 旋转角度（从0度到360度）
            new_rotation = progress * 360.0f * 2f;

            // 缩放因子（从2.0倍大小逐渐缩小到0倍）
            new_scale = 7.2f - progress * 6f;

            // 透明度（保持不变或轻微变化）
            new_alpha = 1.0f;
        }

        rotation = new_rotation;
        scale = new_scale;
        alpha = new_alpha;

        // 启用渲染状态
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableStandardItemLighting();

        // 直接使用OpenGL矩阵操作
        RenderSystem.pushMatrix();
        RenderSystem.translatef(centerX, centerY, 0);
        RenderSystem.rotatef(rotation, 0, 0, 1);
        hud_tools.renderScaledItem(scale, alpha, itemStack, mc);

        // 恢复渲染状态
        RenderSystem.popMatrix();
        RenderHelper.disableStandardItemLighting();
        RenderSystem.disableBlend();

        // 调试信息（每500ms打印一次）
        if (elapsed % 500 < 50) {
            PLOGGER.debug("显示旋转物品HUD，时间: {}ms, 缩放: {}, 旋转: {}", elapsed, scale, rotation);
        }
    }


    /**
     * 检查动画是否已完成
     *
     * @return 如果动画已完成返回true，否则返回false
     */
    public boolean isFinished() {
        if (!Minecraft.getInstance().isGamePaused()) {
            elapsed = HudClientEvent.getEffectiveTime() - startTime;
        }
        boolean finished = elapsed > spendTime; // 1.2秒后动画结束
        if (finished && elapsed < spendTime + 100) { // 只在刚完成时打印一次
            PLOGGER.info("RotatingItemHUD 动画完成");
        }
        return finished;
    }
}
