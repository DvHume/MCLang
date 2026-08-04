package org.dvhume.mclang;

import java.util.HashMap;
import java.util.Map;

/*
 * Copyright (c) 2026 DvHume
 *
 * Licensed under the MIT License.
 * See the LICENSE file in the project root for license information
 */
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
