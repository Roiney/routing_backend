package com.example.routing_backend.domain.enums;

public enum FinanceCategory { // ⚠️ deve ser 'enum', não 'class'

    DELIVERY_FEE("delivery_fee"),
    TIP("tip"),
    BONUS("bonus"),
    FUEL("fuel"),
    MAINTENANCE("maintenance"),
    PARKING("parking"),
    TOLL("toll"),
    FOOD("food"),
    PHONE_DATA("phone_data"),
    VEHICLE_RENT("vehicle_rent"),
    INSURANCE("insurance"),
    OTHER("other");

    private final String value;

    FinanceCategory(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FinanceCategory fromValue(String value) {
        for (FinanceCategory type : FinanceCategory.values()) {
            if (type.value.equalsIgnoreCase(value)) { // permite ignorar maiúsculas/minúsculas
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid FinanceCategory: " + value);
    }
}
