package com.example.keitpanel.common.exception;

public class EquipmentNotFoundException extends RuntimeException {
    public EquipmentNotFoundException(Long id) {
        super("Avadanlıq tapılmadı. id: "+ id);
    }
}
