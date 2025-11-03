package com.example.routing_backend.domain.enums;

public enum PlatformType {
    LOGGI("loggi"),
    MERCADO_LIVRE_FLEX("mercado_livre_flex"),
    AMAZON_FLEX("amazon_flex"),
    LALAMOVE("lalamove"),
    SHOPEE_XPRESS("shopee_xpress"),
    RAPPI("rappi"),
    IFOOD("ifood"),
    UBER_FLASH("uber_flash"),
    OTHER("other");


    private final String value;

    PlatformType(String value) {
     this.value = value;
    }

    public static PlatformType fromValue(String value) {
        for (PlatformType type: PlatformType.values()){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid PlatformType " + value);
    }

}
