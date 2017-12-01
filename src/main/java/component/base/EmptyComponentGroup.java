package main.java.component.base;

class EmptyComponentGroup extends ComponentGroup {
    public EmptyComponentGroup(String key) {
        super(key);
    }

    public Object getDefault() {
        return null;
    }
}
