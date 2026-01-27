package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.AA_MBVD_NpcCapability;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.mojang.blaze3d.matrix.MatrixStack;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NPC_DialogScreen extends Screen {
    public static final NPC_DialogScreen INSTANCE = new NPC_DialogScreen(new StringTextComponent("NPC Dialog"));
    private int entityId;
    private AbstractDialogNode currentNode;

    // 按钮组件
    private Button nextButton;

    protected NPC_DialogScreen(ITextComponent title) {
        super(title);
    }

    @Override
    public void init() {
        super.init();

        // 创建"下一页"按钮
        this.nextButton = new Button(
                this.width / 2 - 50,           // x坐标 - 屏幕中央偏左
                this.height - 50,              // y坐标 - 屏幕底部
                100,                           // 宽度
                20,                            // 高度
                new TranslationTextComponent("screen.aa_mbvd.npc_screen.button.next_node"), // 按钮文字
                button -> nextNode()           // 点击事件
        );

        this.addButton(this.nextButton);

        // 初始隐藏按钮，直到有对话内容时才显示
        updateButtonVisibility();
    }

    private void reloadDialog() {
        if (this.minecraft == null || this.minecraft.world == null) return;

        Entity npc = this.minecraft.world.getEntityByID(entityId);
        if (npc instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) npc;
            mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                if (cap.getCurrentNode() != null) {
                    this.currentNode = cap.getCurrentNode();
                } else {
                    // 检查是否有对话节点
                    if (!cap.getDialogNodes().isEmpty()) {
                        this.currentNode = cap.getDialogNodes().get(0);
                        cap.setCurrentNode(this.currentNode);
                    } else {
                        this.currentNode = AA_MBVD_NpcCapability.defaultDialogNode;
                        if (!cap.addDialogNode(this.currentNode)) {
                            System.out.println("Error: Failed to add default dialog node");
                        } else {
                            cap.setCurrentNode(this.currentNode);
                        }
                    }
                }
            });
        }

        // 更新按钮可见性
        updateButtonVisibility();
    }

    public void nextNode() {
        if (this.minecraft == null || this.minecraft.world == null) return;
        Entity npc = this.minecraft.world.getEntityByID(entityId);
        if (npc instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) npc;
            mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                if (cap.getCurrentNode() != null) {
                    String nextNodeID = cap.getCurrentNode().getNextNodeId();
                    cap.setCurrentNode(cap.getDialogNodeByID(nextNodeID));

                    // 添加空值检查，防止传入null到getDialogNodeByID
                    if (nextNodeID == null || nextNodeID.isEmpty()) {
                        // 如果下一个节点ID为空，可以选择回到第一个节点或显示默认消息
                        if (!cap.getDialogNodes().isEmpty()) {
                            this.currentNode = cap.getDialogNodes().get(0);
                        } else {
                            this.currentNode = AA_MBVD_NpcCapability.defaultDialogNode;
                        }
                    } else {
                        this.currentNode = cap.getDialogNodeByID(nextNodeID);
                    }
                } else {
                    // 添加更详细的错误信息
                    System.out.println("Error: Current node is null when trying to get next node");
                    throw new IllegalArgumentException("Current node is null, cannot proceed to next node");
                }
            });
        }

        // 更新按钮可见性
        updateButtonVisibility();
    }

    /**
     * 更新按钮可见性
     */
    private void updateButtonVisibility() {
        if (this.nextButton != null) {
            this.nextButton.visible = (this.currentNode != null);
        }
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(ms); // 渲染背景

        if (this.minecraft == null || this.minecraft.world == null) return;

        if (currentNode != null) {
            // 计算居中位置
            int centerX = this.width / 2;

            // 渲染角色名称 - 更大的字体
            String roleName = currentNode.getRoleName().getString();
            int roleNameWidth = this.font.getStringWidth(roleName);
            int roleNameX = centerX - roleNameWidth / 2;
            int roleNameY = this.height / 2 - 30; // 屏幕中央偏上

            // 渲染大号角色名称 - 修复方法调用
            this.font.drawString(ms, roleName, roleNameX, roleNameY, 0xFFFF00); // 黄色字体

            // 渲染对话内容 - 在角色名称下方
            String content = currentNode.getContent().getString();
            int contentY = roleNameY + 30; // 在角色名称下方30像素处

            // 将长文本分行显示
            renderTextCentered(ms, content, centerX, contentY, 200); // 最大宽度200像素
        } else {
            // 显示提示信息，表示当前没有可用的对话节点
            String message = "No dialog available";
            int messageWidth = this.font.getStringWidth(message);
            int messageX = (this.width - messageWidth) / 2;
            this.font.drawString(ms, message, messageX, (float) this.height / 2, 0xFF0000); // 修复方法调用
        }

        // 渲染按钮和其他GUI元素
        super.render(ms, mouseX, mouseY, partialTicks);
    }

    /**
     * 渲染居中的多行文本
     * @param ms 矩阵栈
     * @param text 要渲染的文本
     * @param centerX 屏幕中心X坐标
     * @param startY 开始绘制的Y坐标
     * @param maxWidth 最大宽度
     */
    private void renderTextCentered(MatrixStack ms, String text, int centerX, int startY, int maxWidth) {
        // 将长文本分割成适合宽度的多行
        java.util.List<String> lines = wrapText(text, maxWidth);

        int lineHeight = 10; // 字体行高
        int currentY = startY;

        for (String line : lines) {
            // 计算每行的居中X坐标
            int lineWidth = this.font.getStringWidth(line);
            int lineX = centerX - lineWidth / 2;

            // 渲染居中文本 - 修复方法调用
            this.font.drawString(ms, line, lineX, currentY, 0xFFFFFF); // 白色字体

            currentY += lineHeight;
        }
    }


    /**
     * 将文本按指定宽度换行
     * @param text 原始文本
     * @param maxWidth 最大宽度
     * @return 分行后的文本列表
     */
    private java.util.List<String> wrapText(String text, int maxWidth) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split(" ");

        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() > 0 ? currentLine + " " + word : word;

            if (this.font.getStringWidth(testLine) <= maxWidth) {
                currentLine.append(currentLine.length() > 0 ? " " : "").append(word);
            } else {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // 单个词太长，强制截断或按字符分割
                    lines.add(word);
                }
            }
        }

        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
        reloadDialog();
    }

    @Override
    public boolean isPauseScreen() {
        return false; // 不暂停游戏
    }
}
