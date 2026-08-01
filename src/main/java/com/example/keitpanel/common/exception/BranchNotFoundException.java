package com.example.keitpanel.common.exception;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(Long id) {
        super("Filial tapılmadı, id: " + id);
    }
}
