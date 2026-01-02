package com.cookiewyq.aa_mbvd.capability.npc;

import java.util.ArrayList;
import java.util.List;

/**
 * NPC 剧情节点（编辑态 & 运行态共用）
 */
public class EditableNpcStoryNode {

    /** 节点唯一 ID */
    public String id;

    /** 对话内容（ITextComponent 的 JSON 形式） */
    public List<String> dialogJson = new ArrayList<>();

    /** 可接受的证物 ID */
    public List<String> validEvidences = new ArrayList<>();

    /** 正确证物后的下一节点 */
    public String nextNodeId = "";

//    /** 错误证物反馈文本（JSON） */
//    public String wrongTextJson = "{\"text\":\"……\"}";
}