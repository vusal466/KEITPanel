package com.example.keitpanel.common.exception;

public class PositionNotFoundException extends RuntimeException {
    public PositionNotFoundException(Long id) {
        super("vəzifə tapılmadı, id: " + id);
    }
}
