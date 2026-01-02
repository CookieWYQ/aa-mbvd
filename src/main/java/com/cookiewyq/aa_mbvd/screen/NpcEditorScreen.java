package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.container.NpcEditorContainer;
import com.cookiewyq.aa_mbvd.util.Res;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;
import static com.cookiewyq.aa_mbvd.util.Res.SLOT_TEXTURE;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NpcEditorScreen extends ContainerScreen<NpcEditorContainer> {

    private TextFieldWidget npcNameField;
    private TextFieldWidget roleNameField;
    private TextFieldWidget contentField;
    private TextFieldWidget replyField;
    private TextFieldWidget nextNodeField;
    private final int imageWidth = 248; // 7个槽位 * 18 + 边框
    private final int imageHeight = 166; // 标准容器高度
    private final double scaled_x = (double) 231 / 331;
    private final double scaled_y_backpack = (double) 81 / 640;
    private final double scaled_y_hotbar = (double) 89 / 640;

    private final int offset_y = -20;

    public NpcEditorScreen(NpcEditorContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        this.container.setScreen(this);
    }

    public int getOffset_y() {
        return offset_y;
    }

    @Override
    protected void init() {
        super.init();

        PLOGGER.info("?Check isContainerNull: {}, isContainerCapNull: {}", container == null, container != null && container.cap == null);

        // 添加保存按钮
        addButton(new Button(guiLeft, guiTop - 40, 60, 20, new TranslationTextComponent("screen.aa_mbvd.npc_editor.save"), (button) -> saveData()));

        // 添加安全检查，防止container或container.cap为null
//        if (container == null || container.cap == null) {
//            // 如果容器或能力为空，仍然初始化UI组件以避免界面崩溃
//            npcNameField = new TextFieldWidget(font, guiLeft + 120, guiTop + 10 - 5 + offset_y, 120, 20, new StringTextComponent("NPC名称"));
//            roleNameField = new TextFieldWidget(font, guiLeft + 120, guiTop + 35 - 5 + offset_y, 120, 20, new StringTextComponent("角色名称"));
//            contentField = new TextFieldWidget(font, guiLeft + 120, guiTop + 60 - 5 + offset_y, 120, 20, new StringTextComponent("对话内容"));
//            replyField = new TextFieldWidget(font, guiLeft + 120, guiTop + 85 - 5 + offset_y, 120, 20, new StringTextComponent("回复内容"));
//            nextNodeField = new TextFieldWidget(font, guiLeft + 120, guiTop + 110 - 5 + offset_y, 120, 20, new StringTextComponent("下一节点"));
//
//            npcNameField.setText("");
//            roleNameField.setText("");
//            contentField.setText("");
//            replyField.setText("");
//            nextNodeField.setText("");
//
//            addButton(npcNameField);
//            addButton(roleNameField);
//            addButton(contentField);
//            addButton(replyField);
//            addButton(nextNodeField);
//            return;
//        }

        if (container == null) return;

        PLOGGER.info("?In Screen init: container.npc={}, container.cap={}", container.npc, container.cap);
        
        // 如果容器的能力为null，尝试重新获取能力系统
        if (container.npc != null && container.cap == null) {
            PLOGGER.info("?Container capability is null, attempting to retrieve from NPC entity");
            container.npc.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                container.cap = cap;
                PLOGGER.info("?Successfully retrieved capability from NPC entity");
            });
            if (container.cap == null) {
                PLOGGER.warn("?Failed to retrieve NPC capability, this may cause issues");
            }
        }

        AtomicReference<AbstractDialogNode> node = new AtomicReference<>();
        // 添加安全检查，确保container.npc和container.cap不为null
        if (container.npc != null && container.cap != null) {
            node.set(container.cap.getCurrentNode());
        } else {
            // 如果能力仍然为null，使用空节点
            node.set(null);
        }

        // NPC名称输入框
        npcNameField = new TextFieldWidget(font, guiLeft + 120, guiTop + 15 + offset_y, 120, 20, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.npc_name"));
        String npcName = "";
        if (container.cap != null && container.cap.getNpcName() != null) {
            npcName = container.cap.getNpcName().getString();
        }
        npcNameField.setText(npcName);
        addButton(npcNameField);

        // 角色名称输入框
        roleNameField = new TextFieldWidget(font, guiLeft + 120, guiTop + 40 + offset_y, 120, 20, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.role_name"));
        if (node.get() != null) {
            roleNameField.setText(node.get().getRoleName().getString());
        } else {
            roleNameField.setText("No Node");
        }
        addButton(roleNameField);

        // 对话内容输入框
        contentField = new TextFieldWidget(font, guiLeft + 120, guiTop + 65 + offset_y, 120, 20, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.content"));
        if (node.get() != null) {
            contentField.setText(node.get().getContent().getString());
        } else {
            contentField.setText("No Node");
        }
        addButton(contentField);

        // 回复内容输入框
        replyField = new TextFieldWidget(font, guiLeft + 120, guiTop + 90 + offset_y, 120, 20, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.reply"));
        if (node.get() != null) {
            replyField.setText(node.get().getReply().getString());
        } else {
            replyField.setText("No Node");
        }
        addButton(replyField);

        // 下一节点输入框
        nextNodeField = new TextFieldWidget(font, guiLeft + 120, guiTop + 115 + offset_y, 120, 20, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.next_node"));
        if (node.get() != null) {
            UUID nextNodeId = node.get().getNextNodeId();
            if (nextNodeId != null) {
                AbstractDialogNode nextNode = null;
                if (container.cap != null) {
                    nextNode = container.cap.getDialogNode(nextNodeId);
                }
                if (nextNode != null) {
                    nextNodeField.setText(nextNode.getUUID().toString());
                } else {
                    nextNodeField.setText(nextNodeId.toString());
                }
            } else {
                nextNodeField.setText("");
            }
        } else {
            nextNodeField.setText("");
        }
        addButton(nextNodeField);

    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null) return;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);




        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float v, int i1, int i2) {
        if (this.minecraft == null) return;

        // 绘制标签
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("screen.aa_mbvd.npc_editor.title"), this.guiLeft, this.guiTop - 15, 0x404040);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.npc_name"), this.guiLeft, this.guiTop + 15, 0x404040);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.role_name"), this.guiLeft, this.guiTop + 40, 0x404040);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.content"), this.guiLeft, this.guiTop + 65, 0x404040);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.reply"), this.guiLeft, this.guiTop + 90, 0x404040);
        drawString(matrixStack, this.minecraft.fontRenderer, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.next_node"), this.guiLeft, this.guiTop + 115, 0x404040);

        this.minecraft.getTextureManager().bindTexture(Res.BACKGROUND_TEXTURE);
        int guiStartX = (this.width - this.imageWidth) / 2;
        int guiStartY = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, guiStartX, guiStartY, 0, 0, this.imageWidth, this.imageHeight);

        this.minecraft.getTextureManager().bindTexture(SLOT_TEXTURE);

//        // 绘制玩家物品栏槽位背景 (位置固定)
//        for (int i = 0; i < 3; ++i) {
//            for (int j = 0; j < 9; ++j) {
//                this.blit(matrixStack, startX + 231 + j * 18, startY + i * 18 + 81, 0, 0, 18, 18);
//            }
//        }
//
//        // 绘制快捷栏槽位背景 (位置固定)
//        for (int i = 0; i < 9; ++i) {
//            this.blit(matrixStack, startX + 231 + i * 18, startY + 3 * 18 + 89, 0, 0, 18, 18);
//        }

        // 使用容器中定义的起始位置来绘制槽位，添加安全检查
        if (this.container != null) {
            int containerStartX = this.container.getStartX() - 1;
            int containerStartY = this.container.getStartY() - 1;
            
            // 绘制玩家物品栏槽位背景
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    int drawX = guiStartX + containerStartX + (j + 2) * 18;
                    int drawY = guiStartY + containerStartY + i * 18;
                    this.blit(matrixStack, drawX, drawY + offset_y, 0, 0, 18, 18);
                }
            }

            // 绘制快捷栏槽位背景
            for (int i = 0; i < 9; ++i) {
                int drawX = guiStartX + containerStartX + (i + 2) * 18;
                int drawY = guiStartY + containerStartY + 58;
                this.blit(matrixStack, drawX, drawY + offset_y, 0, 0, 18, 18);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {

    }

    private void saveData() {
        // 保存所有输入框的值到能力系统
        if (container != null && container.cap != null) {
            AbstractDialogNode node = container.cap.getCurrentNode();

            // 保存NPC名称
            if (npcNameField != null) {
                String npcNameText = npcNameField.getText();
                container.cap.setNpcName(new StringTextComponent(npcNameText));
            }

            // 保存节点属性（如果节点存在）
            if (node != null) {
                if (roleNameField != null) {
                    String roleNameText = roleNameField.getText();
                    node.setRoleName(new StringTextComponent(roleNameText));
                }
                if (contentField != null) {
                    String contentText = contentField.getText();
                    node.setContent(new StringTextComponent(contentText));
                }
                if (replyField != null) {
                    String replyText = replyField.getText();
                    node.setReply(new StringTextComponent(replyText));
                }
                if (nextNodeField != null) {
                    String text = nextNodeField.getText();
                    if (!text.isEmpty()) {
                        try {
                            node.setNextNodeId(UUID.fromString(text));
                        } catch (IllegalArgumentException e) {
                            // 如果UUID格式不正确，设置为null
                            node.setNextNodeId(null);
                        }
                    } else {
                        node.setNextNodeId(null);
                    }
                }
            }
        }
    }

    @Override
    public void onClose() {
        saveData();
        super.onClose();
    }
}