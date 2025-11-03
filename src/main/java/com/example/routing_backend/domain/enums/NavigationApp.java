package com.example.routing_backend.domain.enums;

public enum NavigationApp {
    GOOGLE_MAPS("google_maps"),
    WAZE("waze"),
    APPLE_MAPS("apple_maps");

    private final String value;

    NavigationApp(String value){
        this.value = value;
    }

    public static NavigationApp fromValue(String value){
        for(NavigationApp type: NavigationApp.values()){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid NavigationApp " + value);
    }
}
