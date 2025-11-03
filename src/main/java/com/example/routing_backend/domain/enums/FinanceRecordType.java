package com.example.routing_backend.domain.enums;

public enum FinanceRecordType {
    REVENUE("revenue"),
    EXPENSE("expense");

    private final String value;

    FinanceRecordType(String value){
        this.value = value;
    }

    public static  FinanceRecordType fromValue(String value){
        for (FinanceRecordType type: FinanceRecordType.values()){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid FinanceRecordType " + value);
    }
}
