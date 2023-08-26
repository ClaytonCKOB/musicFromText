package classes;

public class Action {
    private String name;
    private int value;

    public Action(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Action(String name) {
        this.name = name;
        this.value = 0;
    }

    public String getName() {
        return this.name;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Action: "+ this.name +" | Value: " + this.value;
    }
}
