package com.cookiewyq.aa_mbvd.screen;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.AbstractDialogNode;
import com.cookiewyq.aa_mbvd.capability.npc.nodes.CommonDialogNode;
import com.cookiewyq.aa_mbvd.network.Networking;
import com.cookiewyq.aa_mbvd.network.sendPacks.UpdateAllDialogNodePacket;
import com.cookiewyq.aa_mbvd.network.sendPacks.UpdateDialogNodePacket;
import com.cookiewyq.aa_mbvd.util.Res;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NpcEditorScreen extends Screen {
    public static final NpcEditorScreen INSTANCE = new NpcEditorScreen(new StringTextComponent("NpcEditorScreen"));
    private ArrayList<AbstractDialogNode> dialogNodes = new ArrayList<>();
    private int entityId;
    private String currentNodeId;

    private TextFieldWidget NPC_name_textField;
    private TextFieldWidget NPC_content_textField;
    private TextFieldWidget NPC_thisNode_textField;
    private TextFieldWidget NPC_nextNode_textField;

    private static final ResourceLocation BACKGROUND_LOCATION = Res.BACKGROUND_TEXTURE;

    protected NpcEditorScreen(ITextComponent title) {
        super(title);
    }

    @Override
    public void init() {

        if (this.minecraft != null) {
            this.minecraft.keyboardListener.enableRepeatEvents(true);
        }

        // 初始化文本框组件
        NPC_name_textField = new TextFieldWidget(this.font, this.width / 2, 20, 200, 20, new StringTextComponent(""));
        NPC_content_textField = new TextFieldWidget(this.font, this.width / 2, 50, 200, 20, new StringTextComponent(""));
        NPC_thisNode_textField = new TextFieldWidget(this.font, this.width / 2, 80, 200, 20, new StringTextComponent(""));
        NPC_nextNode_textField = new TextFieldWidget(this.font, this.width / 2, 110, 200, 20, new StringTextComponent(""));

        // 确保文本框被添加到children列表中
        this.children.add(this.NPC_name_textField);
        this.children.add(this.NPC_content_textField);
        this.children.add(this.NPC_thisNode_textField);
        this.children.add(this.NPC_nextNode_textField);

        reloadDialog();
        reloadButtons();
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
        reloadDialog();
        reloadButtons();
    }

    private void reloadDialog() {
        if (this.minecraft == null || this.minecraft.world == null) return;

        Entity npc = this.minecraft.world.getEntityByID(entityId);
        if (npc instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) npc;
            mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                PLOGGER.debug("!!!FUCK RELOAD DIALOG(line85) Data: {}", cap.serializeNBT());
                this.dialogNodes = (ArrayList<AbstractDialogNode>) cap.getDialogNodes();
                if (!this.dialogNodes.isEmpty()) {
                    this.currentNodeId = this.dialogNodes.get(0).getID();
                } else {
                    // 如果没有对话节点，则创建一个默认节点
                    AbstractDialogNode defaultNode = new CommonDialogNode();
                    if (!cap.addDialogNode(defaultNode)) {
                        return;
                    }
                    this.currentNodeId = defaultNode.getID();
                    this.dialogNodes = (ArrayList<AbstractDialogNode>) cap.getDialogNodes();
                }
            });
        }
    }


    private void reloadButtons() {

        if (this.minecraft == null || this.minecraft.world == null) return;

        this.buttons.clear();

        for (int i = 0; i < dialogNodes.size(); i++) {
            int finalI = i;
            this.addButton(new Button(this.width / 2 - 250,
                    i * 35 + 20,
                    100,
                    20,
                    new StringTextComponent(this.dialogNodes.get(i).getID()), button -> {
                this.currentNodeId = this.dialogNodes.get(finalI).getID();
                this.NPC_name_textField.setText(this.dialogNodes.get(finalI).getRoleName().getString());
                this.NPC_content_textField.setText(this.dialogNodes.get(finalI).getContent().getString());
                this.NPC_thisNode_textField.setText(this.dialogNodes.get(finalI).getID());
                this.NPC_nextNode_textField.setText(this.dialogNodes.get(finalI).getNextNodeId());

                // 添加刷新界面的调用
                refreshUI();
            }));
        }

        this.addButton(new Button(this.width / 2 - 100,
                35 + 120,
                100,
                20,
                new TranslationTextComponent("screen.aa_mbvd.npc_editor.button.add"), button -> {
            if (this.minecraft == null || this.minecraft.world == null) return;

            Entity npc = this.minecraft.world.getEntityByID(entityId);
            if (npc instanceof MobEntity) {
                MobEntity mobEntity = (MobEntity) npc;
                mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                    AbstractDialogNode node = new CommonDialogNode();
                    // 生成唯一ID
                    String newId = "node_" + System.currentTimeMillis(); // 生成唯一ID
                    node.setID(newId);
                    cap.addDialogNode(node);
                    this.currentNodeId = newId;

                    // 发送更新包到服务端确保数据持久化
                    sendUpdateToServer(mobEntity, node);
                });
                // 刷新整个界面
                reloadDialog();
                reloadButtons();
            }
        }));

        this.addButton(new Button(this.width / 2,
                35 + 120,
                100,
                20,
                new TranslationTextComponent("screen.aa_mbvd.npc_editor.button.delete"), button -> {
            if (this.minecraft == null || this.minecraft.world == null) return;

            Entity npc = this.minecraft.world.getEntityByID(entityId);
            if (npc instanceof MobEntity) {
                MobEntity mobEntity = (MobEntity) npc;
                mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                    if (cap.getDialogNodes().size() <= 1) return;

                    // 删除当前节点
                    AbstractDialogNode nodeToRemove = cap.getDialogNodeByID(currentNodeId);
                    if (nodeToRemove != null) {
                        cap.removeDialogNode(nodeToRemove);

                        // 设置新的当前节点
                        if (!cap.getDialogNodes().isEmpty()) {
                            AbstractDialogNode firstNode = cap.getDialogNodes().get(0);
                            this.currentNodeId = firstNode.getID();

                            // 更新文本框内容
                            this.NPC_name_textField.setText(firstNode.getRoleName().getString());
                            this.NPC_content_textField.setText(firstNode.getContent().getString());
                            this.NPC_thisNode_textField.setText(firstNode.getID());
                            this.NPC_nextNode_textField.setText(firstNode.getNextNodeId());
                        }


                    }
                });
                // 刷新整个界面
                reloadDialog();
                reloadButtons();
            }
        }));

        this.addButton(new Button(this.width / 2 - 100,
                35 + 145, // 使用动态计算的位置
                200,
                20,
                new TranslationTextComponent("screen.aa_mbvd.npc_editor.button.save"), button -> {
            if (this.minecraft == null || this.minecraft.world == null) return;

            Entity npc = this.minecraft.world.getEntityByID(entityId);
            if (npc instanceof MobEntity) {
                MobEntity mobEntity = (MobEntity) npc;
                mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                    AbstractDialogNode nodeToSave = cap.getDialogNodeByID(currentNodeId);
                    if (nodeToSave != null) {
                        // 保存修改前的ID用于比较
                        String oldId = nodeToSave.getID();

                        // 更新节点内容
                        nodeToSave.setContent(ITextComponent.getTextComponentOrEmpty(NPC_content_textField.getText()));
                        nodeToSave.setRoleName(ITextComponent.getTextComponentOrEmpty(NPC_name_textField.getText()));
                        nodeToSave.setNextNodeId(NPC_nextNode_textField.getText());

                        // 检查是否修改了ID
                        String newId = NPC_thisNode_textField.getText();
                        if (!oldId.equals(newId) || !cap.isDialogNodeExists(newId)) {
                            // 如果ID改变，需要从能力系统中移除旧节点并添加新节点
                            cap.removeDialogNode(nodeToSave);
                            nodeToSave.setID(newId);
                            cap.addDialogNode(nodeToSave);

                            // 更新当前节点ID
                            this.currentNodeId = newId;
                        }

                        sendAllSave(entityId);

                        // 发送更新包到服务端确保数据持久化
                        sendUpdateToServer(mobEntity, nodeToSave);
                    }
                });
                // 只刷新按钮而不重置当前节点
                reloadButtons();
                // 保持当前节点状态
                refreshUI();
            }
        }));


        if (!dialogNodes.isEmpty()) {
            this.NPC_name_textField.setText(dialogNodes.get(0).getRoleName().getString());
            this.NPC_content_textField.setText(dialogNodes.get(0).getContent().getString());
            this.NPC_nextNode_textField.setText(dialogNodes.get(0).getNextNodeId());
            this.NPC_thisNode_textField.setText(dialogNodes.get(0).getID());
        }
    }

    private void sendAllSave(int entityId) {
        Networking.INSTANCE.sendToServer(new UpdateAllDialogNodePacket(entityId));
    }


    /**
     * 发送更新到服务器
     */
    private void sendUpdateToServer(MobEntity mobEntity, AbstractDialogNode node) {
        Networking.INSTANCE.sendToServer(new UpdateDialogNodePacket(
                mobEntity.getEntityId(),
                node.getID(),
                node.getRoleName().getString(),
                node.getContent().getString(),
                node.getNextNodeId()
        ));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        if (this.minecraft == null || this.minecraft.world == null) return;

        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BACKGROUND_LOCATION);

        // 绘制背景图像
        int textureWidth = 208; // 根据实际纹理尺寸调整
        int textureHeight = 156; // 根据实际纹理尺寸调整
        blit(matrixStack, (this.width - 208) / 2, 0, 0, 0, 208, 156, textureWidth, textureHeight);

        this.minecraft.fontRenderer.drawText(matrixStack, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.npc_name"), (float) this.width / 2 - 100, 15, 0xFFFFFF);
        this.minecraft.fontRenderer.drawText(matrixStack, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.content"), (float) this.width / 2 - 100, 45, 0xFFFFFF);
        this.minecraft.fontRenderer.drawText(matrixStack, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.this_node"), (float) this.width / 2 - 100, 75, 0xFFFFFF);
        this.minecraft.fontRenderer.drawText(matrixStack, new TranslationTextComponent("screen.aa_mbvd.npc_editor.description.next_node"), (float) this.width / 2 - 100, 105, 0xFFFFFF);

        this.NPC_nextNode_textField.render(matrixStack, mouseX, mouseY, partialTicks);
        this.NPC_content_textField.render(matrixStack, mouseX, mouseY, partialTicks);
        this.NPC_name_textField.render(matrixStack, mouseX, mouseY, partialTicks);
        this.NPC_thisNode_textField.render(matrixStack, mouseX, mouseY, partialTicks);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
        super.resize(p_231152_1_, p_231152_2_, p_231152_3_);
        if (this.minecraft == null || this.minecraft.world == null) return;
        if (this.entityId == 0) return;

        // 重新初始化文本框位置
        if (NPC_name_textField != null) NPC_name_textField.x = this.width / 2;
        if (NPC_name_textField != null) NPC_name_textField.y = 20;
        if (NPC_content_textField != null) NPC_content_textField.x = this.width / 2;
        if (NPC_content_textField != null) NPC_content_textField.y = 50;
        if (NPC_thisNode_textField != null) NPC_thisNode_textField.x = this.width / 2;
        if (NPC_thisNode_textField != null) NPC_thisNode_textField.y = 80;
        if (NPC_nextNode_textField != null) NPC_nextNode_textField.x = this.width / 2;
        if (NPC_nextNode_textField != null) NPC_nextNode_textField.y = 110;


        reloadDialog();
        reloadButtons();
        updateButtonPositions();
    }


    private void updateButtonPositions() {
        if (this.minecraft == null || this.minecraft.world == null) return;

        // 获取当前屏幕上的按钮列表并更新它们的位置
        for (Widget widget : this.buttons) {
            Button button = (Button) widget;

            // 检查是否是对话节点按钮（通过按钮数量和位置来判断）
            if (isDialogNodeButton(button)) {
                // 找到该按钮对应的数据索引
                int nodeIndex = findDialogNodeIndex(button.getMessage().getString());
                if (nodeIndex != -1) {
                    button.x = this.width / 2 - 250;
                    button.y = nodeIndex * 35 + 20;
                }
            }
            // 特殊处理"Add"、"Delete"和"Save"按钮
            else if (button.getMessage() instanceof TranslationTextComponent &&
                    ((TranslationTextComponent) button.getMessage()).getKey().equals("screen.aa_mbvd.npc_editor.button.add")) {
                button.x = this.width / 2 - 100;
                button.y = dialogNodes.size() * 35 + 120;
            } else if (button.getMessage() instanceof TranslationTextComponent &&
                    ((TranslationTextComponent) button.getMessage()).getKey().equals("screen.aa_mbvd.npc_editor.button.delete")) {
                button.x = this.width / 2;
                button.y = dialogNodes.size() * 35 + 120;
            } else if (button.getMessage() instanceof TranslationTextComponent &&
                    ((TranslationTextComponent) button.getMessage()).getKey().equals("screen.aa_mbvd.npc_editor.button.save")) {
                button.x = this.width / 2 - 100;
                button.y = dialogNodes.size() * 35 + 145;
            }
        }
    }

    /**
     * 判断按钮是否为对话节点按钮
     */
    private boolean isDialogNodeButton(Button button) {
        // 检查按钮文本是否在对话节点列表中
        String buttonText = button.getMessage().getString();
        for (AbstractDialogNode node : dialogNodes) {
            if (node.getID().equals(buttonText)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 查找对话节点在列表中的索引
     */
    private int findDialogNodeIndex(String id) {
        for (int i = 0; i < dialogNodes.size(); i++) {
            if (dialogNodes.get(i).getID().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 刷新UI界面
     */
    private void refreshUI() {
        if (this.minecraft == null || this.minecraft.world == null) return;

        Entity npc = this.minecraft.world.getEntityByID(entityId);
        if (npc instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) npc;
            mobEntity.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).ifPresent(cap -> {
                AbstractDialogNode currentNode = cap.getDialogNodeByID(this.currentNodeId);
                if (currentNode != null) {
                    this.NPC_name_textField.setText(currentNode.getRoleName().getString());
                    this.NPC_content_textField.setText(currentNode.getContent().getString());
                    this.NPC_thisNode_textField.setText(currentNode.getID());
                    this.NPC_nextNode_textField.setText(currentNode.getNextNodeId());
                }
            });
        }
    }


    @Override
    public void tick() {
        super.tick();
        this.NPC_nextNode_textField.tick();
        this.NPC_name_textField.tick();
        this.NPC_content_textField.tick();
        this.NPC_thisNode_textField.tick();


    }

}
