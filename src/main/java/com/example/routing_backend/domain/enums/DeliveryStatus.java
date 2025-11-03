package com.example.routing_backend.domain.enums;

public enum DeliveryStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    FAILED("failed"),
    CANCELLED("cancelled");

    private final String value;

    DeliveryStatus(String value){
        this.value = value;
    }

    public static DeliveryStatus fromValue(String value) {
        for(DeliveryStatus type: DeliveryStatus.values()){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw  new IllegalArgumentException("Invalid DeliveryStatus: " + value);
    }
}
