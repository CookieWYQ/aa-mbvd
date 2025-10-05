package com.cookiewyq.aa_mbvd.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.ITextComponent;

public class Message {
    private ITextComponent message;
    private ITextComponent name;

    public Message(ITextComponent message, ITextComponent name) {
        this.message = message;
        this.name = name;
    }

    public ITextComponent getMessage() {
        return message;
    }

    public ITextComponent getName() {
        return name;
    }

    public void setMessage(ITextComponent message) {
        this.message = message;
    }

    public void setName(ITextComponent name) {
        this.name = name;
    }
}
