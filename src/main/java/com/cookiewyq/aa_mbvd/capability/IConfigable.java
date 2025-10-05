package com.cookiewyq.aa_mbvd.capability;

import com.cookiewyq.aa_mbvd.util.Message;

public interface IConfigable {
    boolean isMovable();
    void setMovable(boolean movable);

    boolean isImmutable();
    void setImmutable(boolean immutable);

    Message[] getMessages();
    void setMessages(Message[] messages);
}
