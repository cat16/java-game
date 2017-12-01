package main.java.component.base;

public class Component<T> {

    private int id;
    private T value;

    public Component(int id, T value) {
        this.id = id;
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

}
