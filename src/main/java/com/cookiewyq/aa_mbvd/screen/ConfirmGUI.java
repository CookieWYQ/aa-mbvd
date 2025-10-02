package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.AA_MbvdMod;
import com.cookiewyq.aa_mbvd.common.BooleanHolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class ConfirmGUI extends Screen {
    private final ITextComponent content;
    private final ITextComponent yes_button_content;
    private final ITextComponent no_button_content;
    private final BooleanHolder result;
    ResourceLocation PORTALHISTORYGUI = new ResourceLocation(AA_MbvdMod.MOD_ID, "textures/gui/gui.png");

    private void init_button() {
        Button yes_button = new Button(this.width / 2 - 150, 200, 100, 20, yes_button_content,
                (button) -> {
                    this.result.set(true);
                    this.closeScreen();
                });

        Button no_button = new Button(this.width / 2 - 50, 300, 100, 20, no_button_content,
                (button) -> {
                    this.result.set(false);
                    this.closeScreen();
                });
        this.addButton(yes_button);
        this.addButton(no_button);
    }

    public ConfirmGUI(BooleanHolder result, ITextComponent titleIn) {
        super(titleIn);
        this.content = new TranslationTextComponent("gui.confirm.default");
        this.yes_button_content = new TranslationTextComponent("gui.confirm.yes");
        this.no_button_content = new TranslationTextComponent("gui.confirm.no");
        this.result = result;
    }

    public ConfirmGUI(BooleanHolder result, ITextComponent titleIn, ITextComponent content) {
        super(titleIn);
        this.content = content;
        this.yes_button_content = new TranslationTextComponent("gui.confirm.yes");
        this.no_button_content = new TranslationTextComponent("gui.confirm.no");
        this.result = result;
    }

    public ConfirmGUI(BooleanHolder result, ITextComponent titleIn, ITextComponent content, ITextComponent yes_button_content, ITextComponent no_button_content) {
        super(titleIn);
        this.content = content;
        this.yes_button_content = yes_button_content;
        this.no_button_content = no_button_content;
        this.result = result;

    }

    @Override
    public void init() {
        if (this.minecraft != null) {
            this.minecraft.keyboardListener.enableRepeatEvents(true);
        }
        init_button();
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        RenderSystem.blendColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bindTexture(PORTALHISTORYGUI);
        }
        int textureWidth = 208;
        int textureHeight = 156;
        blit(matrixStack, this.width / 2 - 150, 10, 0, 0, 300, 200, textureWidth, textureHeight);
        drawCenteredString(matrixStack, this.font, content, 35, 5, 0x4bd0ff);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}