package app.gui.sendform;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SendModel {
    private StringProperty input = new SimpleStringProperty();
    private StringProperty output = new SimpleStringProperty();

    public String getInput() {
        return input.get();
    }

    public StringProperty inputProperty() {
        return input;
    }

    public void setInput(String input) {
        this.input.set(input);
    }

    public String getOutput() {
        return output.get();
    }

    public StringProperty outputProperty() {
        return output;
    }

    public void setOutput(String output) {
        this.output.set(output);
    }
}
