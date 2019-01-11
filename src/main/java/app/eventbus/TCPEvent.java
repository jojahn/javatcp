package app.eventbus;

import java.util.EventObject;

public class TCPEvent extends EventObject {
    public TCPEvent(Object source) {
        super(source);
    }
}
