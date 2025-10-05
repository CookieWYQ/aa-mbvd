package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.capability.CourtRecordContainer;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CourtRecordScreen extends ContainerScreen<CourtRecordContainer> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/gui.png");

    public static final ResourceLocation BACKGROUND_LOCATION =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/slot.png");

    private final int imageWidth = 176;
    private final int imageHeight;
    private final int courtRecordRows;

    public CourtRecordScreen(CourtRecordContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.courtRecordRows = ModConfigs.COURT_RECORD_ROWS.get();
        // 根据行数动态计算GUI高度: 基础高度166 + (行数-3) * 18
        this.imageHeight = 166 + Math.max(0, (courtRecordRows - 3) * 18);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // 绘制GUI背景
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(TEXTURE);
        }

        int startX = (this.width - this.imageWidth) / 2;
        int startY = (this.height - this.imageHeight) / 2;

        // 绘制整个GUI背景
        this.blit(matrixStack, startX, startY, 0, 0, this.imageWidth, this.imageHeight);

        // 绑定槽位背景纹理
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(BACKGROUND_LOCATION);
        }

        // 绘制法庭记录槽位背景（根据实际行数动态绘制）
        for (int i = 0; i < courtRecordRows; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.blit(matrixStack, startX + 7 + j * 18, startY + i * 18 + 6, 0, 0, 18, 18);
            }
        }

        // 绘制玩家物品栏槽位背景 (位置固定)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.blit(matrixStack, startX + 7 + j * 18, startY + (courtRecordRows * 18) - 1 + 8 + i * 18 + 11, 0, 0, 18, 18);
            }
        }

        // 绘制快捷栏槽位背景 (位置固定)
        for (int i = 0; i < 9; ++i) {
            this.blit(matrixStack, startX + 7 + i * 18, startY + (courtRecordRows * 18) + 6 + 3 * 18 + 8 + 11, 0, 0, 18, 18);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int p_230451_2_, int p_230451_3_) {
        this.font.drawText(ms, this.title, (float)(this.imageWidth / 2 - this.font.getStringPropertyWidth(this.title) / 2), 6.0F, 4210752);
        this.font.drawText(ms, this.playerInventory.getDisplayName(), 8.0F, (float)(this.imageHeight - 96 + 2), 4210752);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }
}
