package main.java.component.base;

import java.util.Iterator;

class ComponentIterator<T> {
    private Iterator<Component<T>> iterator;
    private T defaultValue;
    private String key;
    private boolean required;
    private Component<T> current;

    public ComponentIterator(ComponentGroup<T> group, boolean required) {
        this.iterator = group.createComponents().iterator();
        this.key = group.getKey();
        this.required = required;
        this.defaultValue = group.getDefault();
        this.current = null;
    }

    public Component next(){
        Component<T> c = iterator.next();
        current = c;
        return c;
    }

    public boolean hasNext(){
        return iterator.hasNext();
    }

    public String getKey(){
        return key;
    }

    public boolean isRequired(){
        return required;
    }

    public T getDefaultValue(){
        return defaultValue;
    }

    public Component<T> getCurrent(){
        return current;
    }

    public boolean hasCurrent(){
        return current != null;
    }
}
