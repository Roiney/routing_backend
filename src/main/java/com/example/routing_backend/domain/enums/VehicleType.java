package com.example.routing_backend.domain.enums;

public enum VehicleType {
    MOTORCYCLE("motorcycle"),
    CAR("car"),
    VAN("van"),
    BICYCLE("bicycle");

    private final String value;

    VehicleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VehicleType fromValue(String value) {
        for (VehicleType type : VehicleType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid VehicleType: " + value);
    }
}

