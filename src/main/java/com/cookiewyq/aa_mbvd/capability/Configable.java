package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.util.Message;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class Configable implements IConfigable, Capability.IStorage<IConfigable> {
    private boolean movable = false;
    private boolean immutable = false;
    private Message[] messages = new Message[0];

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public void setMovable(boolean movable) {

    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public void setImmutable(boolean immutable) {

    }

    @Override
    public Message[] getMessages() {
        return new Message[0];
    }

    @Override
    public void setMessages(Message[] messages) {

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
// TODO 消息
        return null;
    }

    @Override
    public void readNBT(Capability<IConfigable> capability, IConfigable iConfigable, Direction direction, INBT inbt) {

    }
}
