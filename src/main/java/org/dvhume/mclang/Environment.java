package org.dvhume.mclang;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> variables = new HashMap<>();

    public void set(String name, Object value) {
     variables.put(name, value);
    }

    public Object get(String name) {
        return variables.get(name);
    }

    public boolean has(String name) {
        return variables.containsKey(name);
    }
}
