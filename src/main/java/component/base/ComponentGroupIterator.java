package main.java.component.base;

import java.util.ArrayList;
import java.util.Arrays;

public class ComponentGroupIterator {

    private ComponentIterator baseIterator;
    private ArrayList<ComponentIterator> componentIterators;
    private ArrayList<Component> current;

    public ComponentGroupIterator(ComponentGroup base, ComponentGroup[] componentGroups, String[] requiredComponents){
        this.baseIterator = new ComponentIterator(base, true);
        this.componentIterators = new ArrayList<>();
        ArrayList<String> required = new ArrayList<>(Arrays.asList(requiredComponents));
        for(ComponentGroup group : componentGroups){
            this.componentIterators.add(new ComponentIterator(group, required.contains(group.getKey())));
        }
        this.current = new ArrayList<>();
    }

    public boolean next(){
        if(!baseIterator.hasNext()) return false;
        boolean foundMatches = false;
        while(!foundMatches && baseIterator.hasNext()) {
            current.clear();
            Component c = baseIterator.next();
            foundMatches = true;
            for (ComponentIterator ci : componentIterators) {
                if(ci.hasNext() && (!ci.hasCurrent() || ci.getCurrent().getId() < c.getId())) {
                    Component c2 = ci.next();
                    if (c2.getId() == c.getId()) {
                        current.add(c2);
                    } else if (ci.isRequired()) {
                        foundMatches = false;
                        break;
                    } else {
                        current.add(new Component(c.getId(), ci.getDefaultValue()));
                    }
                }else if(ci.hasCurrent() && ci.getCurrent().getId() == c.getId()){
                    current.add(ci.getCurrent());
                }else{
                    if (ci.isRequired()) {
                        foundMatches = false;
                        break;
                    } else {
                        current.add(new Component(c.getId(), ci.getDefaultValue()));
                    }
                }
            }
            if(foundMatches) return true;
        }
        return false;
    }

    public Component get(String key) {
        if(baseIterator.getKey().equals(key)){
            return baseIterator.getCurrent();
        }
        for(ComponentIterator ci : componentIterators){
            if(ci.getKey().equals(key)){
                return ci.getCurrent();
            }
        }
        return null;
    }
}
