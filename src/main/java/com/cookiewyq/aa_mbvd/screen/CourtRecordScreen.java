package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.configs.ModConfigs;
import com.cookiewyq.aa_mbvd.container.CourtRecordContainer;
import com.cookiewyq.aa_mbvd.util.Pos2D;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CourtRecordScreen extends ContainerScreen<CourtRecordContainer> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/gui.png");

    public static final ResourceLocation BACKGROUND_LOCATION =
            new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/slot.png");

    private final int courtRecordRows = ModConfigs.COURT_RECORD_ROWS.get();

    private int startX;
    private int startY;

    private final Pos2D[] slotPositions = new Pos2D[courtRecordRows * 9 + 27 + 9]; // 添加快捷栏的9个位置

    public CourtRecordScreen(CourtRecordContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getSlot_size() {
        return 18;
    }

    public void initSlotPositions() {
        int index = 0;

        // 绘制法庭记录槽位背景（根据实际行数动态绘制）
        for (int i = 0; i < courtRecordRows; ++i) {
            for (int j = 0; j < 9; ++j) {
                slotPositions[index] = new Pos2D(startX + j * getSlot_size(), startY + getSlot_size() / 2 + i * getSlot_size());
                index++;
            }
        }

        // 绘制玩家物品栏槽位背景 (位置固定)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                slotPositions[index] = new Pos2D(startX + j * getSlot_size(), i * getSlot_size() + (startY + getSlot_size() / 2) + (courtRecordRows - 1) * getSlot_size() + 18 + 18);
                index++;
            }
        }

        // 绘制快捷栏槽位背景 (位置固定)
        for (int i = 0; i < 9; ++i) {
            slotPositions[index] = new Pos2D(startX + i * getSlot_size(), 18 + 18 + 2 * getSlot_size() + (startY + getSlot_size() / 2) + (courtRecordRows - 1) * getSlot_size() + 18 + 9);
            index++;
        }

        this.container.addSlots(this);
    }

    @Deprecated
    // 添加到 CourtRecordContainer 类中
    public static Pos2D calculateSlotPosition(int index, int courtRecordRows, int startX, int startY) {
        int slotSize = 18;

        if (index == 0) index = 1;

        if (0 < index && index < courtRecordRows * 9) {
            // 法庭记录槽位 (从第1行开始)
            int row = index / 9;
            int col = index % 9;
            return new Pos2D(startX + col * slotSize, startY + slotSize + row * slotSize);
        } else if (index >= courtRecordRows * 9 && index < courtRecordRows * 9 + 27) {
            // 玩家物品栏槽位 (3行)
            int localIndex = index - courtRecordRows * 9;
            int row = localIndex / 9;
            int col = localIndex % 9;
            return new Pos2D(startX + col * slotSize,
                    startY + slotSize + (courtRecordRows - 1) * slotSize + 18 + 18 + row * slotSize);
        } else if (index >= courtRecordRows * 9 + 27 && index < courtRecordRows * 9 + 36) {
            // 快捷栏槽位 (1行)
            int col = index - courtRecordRows * 9 - 27;
            return new Pos2D(startX + col * slotSize,
                    startY + slotSize + (courtRecordRows - 1) * slotSize + 18 + 18 + 54 + 18);
        } else {
            // 超出范围
            return new Pos2D(startX, startY);
        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // 绘制GUI背景
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        int imageHeight = 166;
        this.initSlotPositions();

        int imageWidth = 176;
        startX = (this.width - imageWidth) / 2;
        startY = (this.height - imageHeight) / 2;

        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(TEXTURE);
        }

        // 绘制整个GUI背景
        this.blit(matrixStack, startX, startY, 0, 0, imageWidth, imageHeight);

        this.font.drawText(matrixStack, this.title, startX, startY, 4210752);
        this.font.drawText(matrixStack, this.playerInventory.getDisplayName(), startX, (startY + (float) getSlot_size() / 2) + (courtRecordRows - 1) * getSlot_size() + 27, 4210752);

        // 绑定槽位背景纹理
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(BACKGROUND_LOCATION);
        }

        // 使用统一方法绘制槽位背景并存储坐标
        for (Pos2D pos : slotPositions) {
            this.blit(matrixStack, pos.getX(), pos.getY(), 0, 0, 18, 18);
        }
    }

    public Pos2D getSlotPosition(int slotIndex) {
        PLOGGER.warn("!!! Pos2D From Screen getter index:{}, value:{}", slotIndex, slotPositions[slotIndex]);
        return slotPositions[slotIndex];
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack ms, int p_230451_2_, int p_230451_3_) {

    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
    }
}
