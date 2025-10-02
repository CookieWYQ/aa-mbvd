package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ChainLockHUD extends AbstractGui {
    private static final Minecraft mc = Minecraft.getInstance();
    
    // 贴图资源
    private static final ResourceLocation CHAIN_TEXTURE = 
        new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/hud/chain.png");
    private static final ResourceLocation RED_LOCK_TEXTURE = 
        new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/hud/red_lock.png");
    private static final ResourceLocation BLACK_LOCK_TEXTURE = 
        new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/hud/black_lock.png");
    
    private final MatrixStack matrixStack;
    private final int lockCount;
    private final LockType lockType;
    private final long startTime;
    
    // 锁链和锁的尺寸
    private static final int CHAIN_WIDTH = 16;
    private static final int CHAIN_HEIGHT = 16;
    private static final int LOCK_WIDTH = 32;
    private static final int LOCK_HEIGHT = 32;
    
    // 动画参数
    private static final int ANIMATION_DURATION = 2000; // 2秒动画
    private static final float CHAIN_SPEED = 200.0f; // 锁链移动速度

    public enum LockType {
        RED, BLACK
    }

    public ChainLockHUD(MatrixStack matrixStack, int lockCount, LockType lockType) {
        this.matrixStack = matrixStack;
        this.lockCount = Math.max(1, lockCount); // 确保至少为1
        this.lockType = lockType;
        this.startTime = System.currentTimeMillis();
    }

    public void show() {
        MatrixStack ms = this.matrixStack;
        
        // 获取屏幕尺寸
        int screenWidth = mc.getMainWindow().getScaledWidth();
        int screenHeight = mc.getMainWindow().getScaledHeight();
        
        // 计算动画进度 (0.0 - 1.0)
        long elapsed = System.currentTimeMillis() - startTime;
        float progress = Math.min(1.0f, (float) elapsed / ANIMATION_DURATION);
        
        // 计算起始和结束位置
        // 起始点：屏幕斜上方
        int startX = screenWidth + 100;
        int startY = -100;
        
        // 结束点：屏幕下方居中偏下位置
        int endX = screenWidth / 2;
        int endY = screenHeight + 50;
        
        // 计算锁链的总长度
        float totalDistance = (float) Math.sqrt(
            Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        
        // 计算锁链角度
        float angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
        
        // 绑定锁链纹理
        mc.getTextureManager().bindTexture(CHAIN_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        
        // 绘制多条锁链（根据锁的数量）
        for (int i = 0; i < lockCount; i++) {
            // 计算每条锁链的偏移量，使它们交叉
            float offsetAngle = (i - (lockCount - 1) / 2.0f) * 15.0f; // 每条锁链间隔15度
            
            // 计算实际的起始和结束点
            float actualStartX = startX + (float) (Math.cos(Math.toRadians(offsetAngle - 90)) * 20 * i);
            float actualStartY = startY + (float) (Math.sin(Math.toRadians(offsetAngle - 90)) * 20 * i);
            float actualEndX = endX + (float) (Math.cos(Math.toRadians(offsetAngle - 90)) * 20 * i);
            float actualEndY = endY + (float) (Math.sin(Math.toRadians(offsetAngle - 90)) * 20 * i);
            
            // 计算当前动画位置
            float currentX = actualStartX + (actualEndX - actualStartX) * progress;
            float currentY = actualStartY + (actualEndY - actualStartY) * progress;
            
            // 绘制锁链
            drawChains(ms, actualStartX, actualStartY, currentX, currentY, angle + offsetAngle);
            
            // 绘制锁（在动画结束时显示）
            if (progress >= 1.0f) {
                drawLock(ms, actualEndX, actualEndY, offsetAngle);
            }
        }
        
        RenderSystem.disableBlend();
    }
    
    /**
     * 绘制锁链
     */
    private void drawChains(MatrixStack ms, float startX, float startY, float endX, float endY, float angle) {
        // 计算锁链长度
        float chainLength = (float) Math.sqrt(
            Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        
        // 保存当前矩阵状态
        ms.push();
        ms.translate(startX, startY, 0);
        ms.rotate(Vector3f.ZP.rotationDegrees(angle));
        
        // 绘制连续的锁链段
        float drawnLength = 0;
        while (drawnLength < chainLength) {
            // 计算当前段的位置
            float segmentX = drawnLength;
            float segmentY = 0;
            
            // 如果剩余长度小于一个锁链段，则调整宽度
            float drawWidth = Math.min(CHAIN_WIDTH, chainLength - drawnLength);
            
            // 绘制锁链段
            blit(ms, (int)segmentX, (int)segmentY, 0, 0, (int)drawWidth, CHAIN_HEIGHT, CHAIN_WIDTH, CHAIN_HEIGHT);
            
            drawnLength += CHAIN_WIDTH;
        }
        
        ms.pop();
    }
    
    /**
     * 绘制锁
     */
    private void drawLock(MatrixStack ms, float x, float y, float angle) {
        // 选择锁的纹理
        ResourceLocation lockTexture = (lockType == LockType.RED) ? RED_LOCK_TEXTURE : BLACK_LOCK_TEXTURE;
        mc.getTextureManager().bindTexture(lockTexture);
        
        ms.push();
        ms.translate(x, y, 0);
        ms.rotate(Vector3f.ZP.rotationDegrees(angle));
        
        // 绘制锁（居中）
        blit(ms, -LOCK_WIDTH / 2, -LOCK_HEIGHT / 2, 0, 0, LOCK_WIDTH, LOCK_HEIGHT, LOCK_WIDTH, LOCK_HEIGHT);
        
        ms.pop();
        
        // 重新绑定锁链纹理，为下一次绘制做准备
        mc.getTextureManager().bindTexture(CHAIN_TEXTURE);
    }
    
    /**
     * 检查动画是否已完成
     */
    public boolean isFinished() {
        long elapsed = System.currentTimeMillis() - startTime;
        return elapsed > ANIMATION_DURATION;
    }
}
