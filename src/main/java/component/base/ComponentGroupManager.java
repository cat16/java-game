package main.java.component.base;

import java.util.ArrayList;
import java.util.Arrays;

public class ComponentGroupManager {
    public ArrayList<ComponentGroup> componentGroups;

    public ComponentGroupManager(){
        componentGroups = new ArrayList<>();
    }

    public ComponentGroupManager add(ComponentGroup group){
        componentGroups.add(group);
        return this;
    }

    public ComponentGroup get(String key){
        for(ComponentGroup group : componentGroups){
            if(group.getKey() == key){
                return group;
            }
        }
        return null;
    }

    public ComponentGroupIterator createGroupIterator(String base, String[] keys, String[] required){
        ComponentGroup baseGroup = get(base);
        if(baseGroup == null) return null;

        ArrayList<String> keysAL = new ArrayList<>(Arrays.asList(keys));
        ArrayList<String> requiredAL = new ArrayList<>(Arrays.asList(required));

        requiredAL.forEach(key -> {
            if(!keysAL.contains(key)) keysAL.add(key);
        });
        ArrayList<ComponentGroup> groups = new ArrayList<>();
        for(String key : keysAL){
            ComponentGroup componentGroup = get(key);
            if(componentGroup == null){
                if(new ArrayList<String>(requiredAL).contains(key)){
                    return null;
                }else{
                    groups.add(new EmptyComponentGroup(key));
                }
            }else{
                groups.add(componentGroup);
            }
        }
        return new ComponentGroupIterator(baseGroup, groups.toArray(new ComponentGroup[groups.size()]), requiredAL.toArray(new String[requiredAL.size()]));
    }
}
