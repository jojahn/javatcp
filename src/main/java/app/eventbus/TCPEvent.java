package app.eventbus;

import java.util.EventObject;

public class TCPEvent extends EventObject {
    public static final short SEND_TAG = 0;
    public static final short RECIEVE_TAG = 1;

    private short tag = -1;
    private String message;

    public TCPEvent(String message, short tag) {
        super(message);
        this.message = message;
        this.tag = tag;
    }

    public short getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }
}
