package main.java.component.base;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public abstract class ComponentGroup<T> {

    private String key;
    private HashMap<Integer, T> components;

    public ComponentGroup(String key){
        this.key = key;
        components = new HashMap<>();
    }

    public ComponentGroup<T> addComponent(int id, T value){
        components.put(id, value);
        return this;
    }

    public ArrayList<Component<T>> createComponents(){
        ArrayList<Component<T>> components = new ArrayList<>();
        for(Map.Entry<Integer, T> s : this.components.entrySet()){
            components.add(new Component<T>(s.getKey(), s.getValue()));
        }
        components.sort(Comparator.comparingInt(Component::getId));
        return components;
    }

    public T getComponent(int id){
        return components.getOrDefault(id, getDefault());
    }
    public T[] getCompoents() {return ((T[]) components.values().toArray());}

    public String getKey(){
        return key;
    }

    public abstract T getDefault();

}
