package com.example.routing_backend.domain.enums;

public enum RouteStatus {
    PENDING("pending"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    RouteStatus(String value){
        this.value = value;
    }

    public static RouteStatus fromValue(String value){
        for( RouteStatus type : RouteStatus.values()){
            if (type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid RouteStatus: " + value);
    }

}
