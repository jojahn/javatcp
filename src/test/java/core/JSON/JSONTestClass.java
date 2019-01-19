package core.JSON;

import java.io.Serializable;

public class JSONTestClass implements Serializable {
    private int id;
    private String name;

    public JSONTestClass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public JSONTestClass() {}

    @Override
    public String toString() {
        return id+","+name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
