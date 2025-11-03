package com.example.routing_backend.domain.enums;

public enum SubscriptionType {
    FREE("free"),
    PREMIUM("premium"),
    TRIAL("trial");

    private final String value;

    SubscriptionType(String value) {
        this.value = value;
    }

    public  static SubscriptionType fromValue(String value){
        for ( SubscriptionType type : SubscriptionType.values()){
            if (type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid SubscriptionType: " + value);
    }
}
