package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.common.BooleanHolder;
import com.cookiewyq.aa_mbvd.common.ObservableValue;
import com.cookiewyq.aa_mbvd.container.CourtRecordsContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.cookiewyq.aa_mbvd.container.CourtRecordsContainer.GUI_HEIGHT;
import static com.cookiewyq.aa_mbvd.container.CourtRecordsContainer.GUI_WIDTH;

@OnlyIn(Dist.CLIENT)
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CourtRecordsGUI extends ContainerScreen<CourtRecordsContainer> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/gui.png"); // container/court_records.png

    public static final BooleanHolder xChangeHolder = new BooleanHolder();
    public static final BooleanHolder yChangeHolder = new BooleanHolder();
    public static final ObservableValue<Integer> xHolder = new ObservableValue<>(0,
            () -> xChangeHolder.set(true));
    public static final ObservableValue<Integer> yHolder = new ObservableValue<>(0,
            () -> yChangeHolder.set(true));

    public CourtRecordsGUI(CourtRecordsContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void init() {
        xHolder.set((this.width - this.xSize) / 2);
        yHolder.set((this.height - this.ySize) / 2);

        this.container.setupSlots(xHolder.get(), yHolder.get());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float v, int i, int i1) {
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
            // 绘制GUI背景，使用新的尺寸
            int x = (this.width - GUI_WIDTH) / 2;
            int y = (this.height - GUI_HEIGHT) / 2;
            this.blit(ms, x, y, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        }
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
    }
}
