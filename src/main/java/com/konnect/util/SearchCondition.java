package com.konnect.util;

import java.util.HashMap;
import java.util.Map;

public class SearchCondition {

    private final Map<String, String> conditions = new HashMap<>();

    public void put(String key, String value) {
        if (value != null && !value.isBlank()) {
            conditions.put(key, value);
        }
    }

    public String get(String key) {
        return conditions.get(key);
    }

    public boolean has(String key) {
        String value = conditions.get(key);
        return value != null && !value.isBlank();
    }

    public Map<String, String> getAll() {
        return conditions;
    }
}
