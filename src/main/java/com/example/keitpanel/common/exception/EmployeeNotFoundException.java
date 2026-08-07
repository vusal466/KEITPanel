package com.example.keitpanel.common.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("İşçi tapılmadı, id: " + id);
    }
}
