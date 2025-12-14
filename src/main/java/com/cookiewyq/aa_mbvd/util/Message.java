package com.cookiewyq.aa_mbvd.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class Message {
    private TranslationTextComponent message;
    private TranslationTextComponent name;

    public Message(TranslationTextComponent message, TranslationTextComponent name) {
        this.message = message;
        this.name = name;
    }

    public Message() {
        this.message = null;
        this.name = null;
    }

    public ITextComponent getMessage() {
        return message;
    }

    public ITextComponent getName() {
        return name;
    }

    public void setMessage(TranslationTextComponent message) {
        this.message = message;
    }

    public void setName(TranslationTextComponent name) {
        this.name = name;
    }

    public CompoundNBT writeNBT(CompoundNBT nbt) {
        CompoundNBT messageNBT = new CompoundNBT();
        messageNBT.put("message", tools.writeToNBT(message));
        messageNBT.put("name", tools.writeToNBT(name));
        nbt.put("message", messageNBT);
        return nbt;
    }

    public void readNBT(CompoundNBT nbt) {
        CompoundNBT messageNBT = nbt.getCompound("message");
        message = tools.readFromNBT((CompoundNBT) messageNBT.get("message"));
        name = tools.readFromNBT((CompoundNBT) messageNBT.get("name"));
    }
}
