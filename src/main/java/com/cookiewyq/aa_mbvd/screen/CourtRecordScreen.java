package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
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
    public static final ResourceLocation BACKGROUND_TEXTURE =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/gui.png");
    public static final ResourceLocation SLOT_TEXTURE =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/slot.png");

    private final int imageWidth;
    private final int imageHeight;
    private final int courtRecordRows;

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
        this.courtRecordRows = 4;
        // 设置GUI大小
        this.imageWidth = 248; // 7个槽位 * 18 + 边框
        this.imageHeight = 166; // 标准容器高度
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        startX = (this.width - this.imageWidth) / 2;
        startY = (this.height - this.imageHeight) / 2;

//        if (this.minecraft != null) {
//            this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
//        }
//        int textureWidth = 208;
//        int textureHeight = 156;
//        blit(ms, this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);

        // 绘制槽位纹理
//        if (this.minecraft != null) {
//            this.minecraft.getTextureManager().bindTexture(SLOT_TEXTURE);
//        }
//        this.blit(ms, startX, startY, 0, 0, this.imageWidth, this.imageHeight);

        // 绘制左上角标题文字（根据饰品类型显示不同文字）
        ITextComponent titleText = isAttorneysBadge ? new TranslationTextComponent("container.aa_mbvd.court_record.attorneys_badge") :
                new TranslationTextComponent("container.aa_mbvd.court_record.prosbadge");
        this.font.drawText(ms, titleText, startX + 8, startY + 6, 0x404040);

        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float partialTicks, int mouseX, int mouseY) {
        // 绘制GUI背景
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        }

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        // 绘制整个GUI背景
        this.blit(ms, startX, startY, 0, 0, this.imageWidth, this.imageHeight);

        // 绑定槽位背景纹理
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(SLOT_TEXTURE);
        }

        // 绘制法庭记录槽位背景（根据实际行数动态绘制）
        for (int i = 0; i < courtRecordRows; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.blit(ms, startX + 7 + j * 18 + 36, startY + i * 18 + 17, 0, 0, 18, 18);
            }
        }

        // 绘制玩家物品栏槽位背景 (位置固定)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.blit(ms, startX + 7 + j * 18 + 36, startY + (courtRecordRows * 18) - 1 + 8 + i * 18 + 22 - 2, 0, 0, 18, 18);
            }
        }

        // 绘制快捷栏槽位背景 (位置固定)
        for (int i = 0; i < 9; ++i) {
            this.blit(ms, startX + 7 + i * 18 + 36, startY + (courtRecordRows * 18) + 6 + 3 * 18 + 8 + 22 - 5, 0, 0, 18, 18);
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(MatrixStack ms, int x, int y) {
        
    }
}