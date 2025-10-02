package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.items.custom.photos.PhotoHelper;
import com.cookiewyq.aa_mbvd.items.custom.photos.PhotoItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

public class PhotoShowingScreen extends AbstractGui {
    private static final Minecraft mc = Minecraft.getInstance();

    private final ResourceLocation BG;
    private final PhotoItem photoItem;
    private final MatrixStack ms;

    public PhotoShowingScreen(MatrixStack matrixStack, PhotoItem photoItem) {
        this.photoItem = photoItem;
        this.BG = photoItem.getResourceLocation();
        this.ms = matrixStack;
    }

    public PhotoShowingScreen(MatrixStack matrixStack, ItemStack photoItemStack) {
        if (!(photoItemStack.getItem() instanceof PhotoItem)) {
            throw new IllegalArgumentException("photoItemStack must be a PhotoItem");
        }
        this.photoItem = (PhotoItem) photoItemStack.getItem();
        this.BG = photoItem.getResourceLocation();
        this.ms = matrixStack;
    }

    public void show() {
        // 获取屏幕尺寸
        int screenWidth = mc.getMainWindow().getScaledWidth();
        int screenHeight = mc.getMainWindow().getScaledHeight();

        // 获取图片实际尺寸
//        int imageWidth = photoItem.getPhotoWidth() / 2;
//        int imageHeight = photoItem.getPhotoHeight() / 2;

//        int imageWidth = mc.getMainWindow().getScaledWidth();
//        int imageHeight = mc.getMainWindow().getScaledHeight();

        int[] fittingSize = PhotoHelper.getFittingSize(photoItem.getPhotoWidth(), photoItem.getPhotoHeight(), screenWidth, screenHeight);
        int imageWidth = fittingSize[0];
        int imageHeight = fittingSize[1];
        PLOGGER.info("imageWidth: {}, imageHeight: {}", imageWidth, imageHeight);

        // 计算居中位置
        int x = (screenWidth - imageWidth) / 2;
        int y = (screenHeight - imageHeight) / 2;

        // 启用混合和纹理
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 0);
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);

        // 绑定纹理并绘制
        mc.getTextureManager().bindTexture(BG);
        blit(ms, x, y, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

        // 恢复渲染状态
        RenderSystem.disableBlend();
    }

    public ResourceLocation getBG() {
        return BG;
    }

    public PhotoItem getPhotoItem() {
        return photoItem;
    }
}
