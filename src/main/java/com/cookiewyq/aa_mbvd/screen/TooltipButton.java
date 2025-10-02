package com.cookiewyq.aa_mbvd.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class TooltipButton extends Button {
    private ITextComponent tooltip;
    private BlockPos pos;

    public TooltipButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITextComponent tooltip) {
        super(x, y, width, height, title, pressedAction);
        this.tooltip = tooltip;
    }

    public TooltipButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip onTooltip, ITextComponent tooltip) {
        super(x, y, width, height, title, pressedAction, onTooltip);
        this.tooltip = tooltip;
    }

    public void _setPos(BlockPos pos){
        this.pos = pos;
    }

    public BlockPos getPos(){
        return this.pos;
    }

    public void setTooltip(ITextComponent tooltip) {
        this.tooltip = tooltip;
    }

    public ITextComponent getTooltip() {
        return tooltip;
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (tooltip != null && this.isHovered()) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer font = minecraft.fontRenderer;
            Screen screen = minecraft.currentScreen;
            
            if (screen != null) {
                // 绘制提示框背景
                int tooltipWidth = font.getStringPropertyWidth(tooltip) + 6;
                int tooltipHeight = 8 + 6;
                int tooltipX = mouseX + 12;
                int tooltipY = mouseY - 12;
                
                // 确保提示框不会超出屏幕边界
                if (tooltipX + tooltipWidth > screen.width) {
                    tooltipX = screen.width - tooltipWidth;
                }
                if (tooltipY + tooltipHeight > screen.height) {
                    tooltipY = screen.height - tooltipHeight;
                }
                if (tooltipY < 0) {
                    tooltipY = 0;
                }
                
                // 绘制背景（使用公共方法）
                fillGradient(matrixStack, tooltipX, tooltipY, tooltipX + tooltipWidth, tooltipY + tooltipHeight, 0xF0100010, 0xF0100010);
                fillGradient(matrixStack, tooltipX, tooltipY, tooltipX + tooltipWidth, tooltipY + 1, 0x505000FF, 0x5028007F);
                fillGradient(matrixStack, tooltipX, tooltipY + tooltipHeight - 1, tooltipX + tooltipWidth, tooltipY + tooltipHeight, 0x505000FF, 0x5028007F);
                
                // 绘制文本
                font.drawStringWithShadow(matrixStack, tooltip.getString(), tooltipX + 3, tooltipY + 3, 0xFFFFFF);
            }
        }
        super.renderToolTip(matrixStack, mouseX, mouseY);
    }
}