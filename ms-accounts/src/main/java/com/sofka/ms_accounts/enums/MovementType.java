package com.sofka.ms_accounts.enums;

public enum MovementType {
    DEPOSITO("depósito"),
    RETIRO("retiro");

    private final String value;

    MovementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MovementType fromValue(String value) {
        for (MovementType type : MovementType.values()) {
            if (type.getValue().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de movimiento no válido: " + value);
    }
}
