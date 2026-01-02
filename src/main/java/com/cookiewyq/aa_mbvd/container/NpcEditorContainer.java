package com.cookiewyq.aa_mbvd.container;

import com.cookiewyq.aa_mbvd.capability.Capabilities;
import com.cookiewyq.aa_mbvd.capability.npc.IAA_MBVD_NpcCapability;
import com.cookiewyq.aa_mbvd.screen.NpcEditorScreen;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.cookiewyq.aa_mbvd.AA_MbvdMod.PLOGGER;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NpcEditorContainer extends Container {

    public Entity npc;
    public IAA_MBVD_NpcCapability cap;
    public final PlayerInventory playerInventory;
    private NpcEditorScreen screen;
    private int startX;
    private int startY;
    private boolean hasLayout = false;

    public NpcEditorContainer(int id, PlayerInventory inv, MobEntity npc) {
        super(ModContainerTypes.NPC_EDITOR_CONTAINER.get(), id);
        this.playerInventory = inv;
        PLOGGER.info("?Check Create Container with NPC");
        
        // 直接获取NPC的能力，使用安全方式处理可能缺失的能力
        this.cap = npc.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).orElse(null);

        setNpc(npc);
        
        // 初始化坐标，与无参构造函数保持一致
        startX = 8;
        startY = 163;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public NpcEditorContainer(int id, PlayerInventory inv) {
        super(ModContainerTypes.NPC_EDITOR_CONTAINER.get(), id);
        this.playerInventory = inv;

        startX = 8;
        startY = 163;
    }

    public void setScreen(NpcEditorScreen screen) {
        this.screen = screen;
        if (!hasLayout){
            layoutPlayerInventory(playerInventory, startX, startY + this.screen.getOffset_y());
            hasLayout = true;
        }
    }

    private void layoutPlayerInventory(PlayerInventory inv, int x, int y) {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 9; c++)
                addSlot(new Slot(inv, c + r * 9 + 9, x + c * 18, y + r * 18));

        for (int c = 0; c < 9; c++)
            addSlot(new Slot(inv, c, x + c * 18, y + 58));
    }

    public void setNpc(Entity npc) {
        this.npc = npc;
        this.cap = npc.getCapability(Capabilities.AA_MBVD_NPC_CAPABILITY).orElseThrow(() -> new RuntimeException("No Npc Capability"));
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }
}