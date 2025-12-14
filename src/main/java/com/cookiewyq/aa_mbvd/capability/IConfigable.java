package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.util.Message;

import java.util.ArrayList;

public interface IConfigable {
    boolean isMovable();
    void setMovable(boolean movable);

    boolean isImmutable();
    void setImmutable(boolean immutable);

    ArrayList<Message> getMessages();
    void setMessages(ArrayList<Message> messages);
}
