package app.eventbus;

import java.util.EventObject;

public class OutputEvent extends EventObject {
    private String output;

    public OutputEvent(String source) {
        super(source);
        this.output = source;
    }

    public String getOutput() {
        return this.output;
    }
}