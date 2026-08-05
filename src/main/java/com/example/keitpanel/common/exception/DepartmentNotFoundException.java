package com.example.keitpanel.common.exception;

public class DepartmentNotFoundException extends RuntimeException{
    public DepartmentNotFoundException(Long id){
        super("Şöbə tapılmadı: " + id);
    }
}
