package com.example.routing_backend.domain.enums;

public enum AccountStatus {
    ACTIVE("active"),
    INACTIVE("inactive"),
    SUSPENDED("suspended"),
    PENDING("pending");

    private final String value;

    AccountStatus(String value){
        this.value = value;
    }

    public static AccountStatus fromValue(String value){
        for (AccountStatus type: AccountStatus.values()){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid AccountStatus " + value);
    }
}
