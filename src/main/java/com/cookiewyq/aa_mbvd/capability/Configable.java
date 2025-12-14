package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.util.Message;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class Configable implements IConfigable, Capability.IStorage<IConfigable> {
    private boolean movable = false;
    private boolean immutable = false;
    private ArrayList<Message> messages = new ArrayList<>();

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    @Override
    public boolean isImmutable() {
        return immutable;
    }

    @Override
    public void setImmutable(boolean immutable) {
        this.immutable = immutable;
    }

    @Override
    public ArrayList<Message> getMessages() {
        return messages;
    }

    @Override
    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public static class Storage implements Capability.IStorage<Configable> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<Configable> capability, Configable configable, Direction direction) {
            return capability.writeNBT(configable, direction);
        }

        @Override
        public void readNBT(Capability<Configable> capability, Configable configable, Direction direction, INBT inbt) {
            capability.readNBT(configable, direction, inbt);
        }
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IConfigable> capability, IConfigable iConfigable, Direction direction) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("movable", this.movable);
        nbt.putBoolean("immutable", this.immutable);
        CompoundNBT messageNBT = new CompoundNBT();
        for (Message message : this.messages) {
            message.writeNBT(messageNBT);
        }
        nbt.put("messages", messageNBT);
        nbt.putInt("size", this.messages.size());
        return nbt;
    }

    @Override
    public void readNBT(Capability<IConfigable> capability, IConfigable iConfigable, Direction direction, INBT inbt) {
        CompoundNBT nbt = (CompoundNBT) inbt;
        this.movable = nbt.getBoolean("movable");
        this.immutable = nbt.getBoolean("immutable");
        int size = nbt.getInt("size");
        CompoundNBT messageNBT = nbt.getCompound("messages");
        this.messages.clear();
        for (int i = 0; i < size; i++) {
            Message message = new Message();
            message.readNBT(messageNBT);
            this.messages.add(message);
        }
    }
}
