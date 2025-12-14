package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.container.CourtRecordContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CourtRecordScreen extends ContainerScreen<CourtRecordContainer> {
    // GUI纹理路径 - 使用指定的gui.png作为背景
    private static final ResourceLocation BACKGROUND_TEXTURE =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/gui.png");
    private static final ResourceLocation SOLT_TEXTURE =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/solt.png");

    private final int imageWidth;
    private final int imageHeight;

    public int getStartY() {
        return startY;
    }

    public int getStartX() {
        return startX;
    }

    private int startX;
    private int startY;

    private final boolean isAttorneysBadge;

    public CourtRecordScreen(CourtRecordContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.isAttorneysBadge = container.isAttorneysBadge();
        // 设置GUI大小
        this.imageWidth = 248; // 7个槽位 * 18 + 边框
        this.imageHeight = 166; // 标准容器高度
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        startX = (this.width - this.imageWidth) / 2;
        startY = (this.height - this.imageHeight) / 2;

        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        }
        int textureWidth = 208;
        int textureHeight = 156;
        blit(matrixStack, this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);

        // 绘制槽位纹理
//        if (this.minecraft != null) {
//            this.minecraft.getTextureManager().bindTexture(SOLT_TEXTURE);
//        }
//        this.blit(matrixStack, startX, startY, 0, 0, this.imageWidth, this.imageHeight);

        // 绘制左上角标题文字（根据饰品类型显示不同文字）
        ITextComponent titleText = isAttorneysBadge ? new TranslationTextComponent("container.aa_mbvd.court_record.attorneys_badge") :
                new TranslationTextComponent("container.aa_mbvd.court_record.prosbadge");
        this.font.drawText(matrixStack, titleText, startX + 8, startY + 6, 0x404040);

        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {

    }

    @Override
    public void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {

    }
}