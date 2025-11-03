package com.example.routing_backend.domain.enums;

public enum OptimizationType {
    SHORTEST_DISTANCE("shortest_distance"),
    SHORTEST_TIME("shortest_time"),
    RESPECT_TIME_WINDOWS("respect_time_windows"),
    BALANCED("balanced");

    private final String value;

    OptimizationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OptimizationType fromValue(String value) {
        for (OptimizationType type : OptimizationType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid OptimizationType: " + value);
    }
}
