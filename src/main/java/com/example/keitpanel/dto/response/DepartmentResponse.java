package com.example.keitpanel.dto.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentResponse {

    private String name;
    private String code;
    private boolean active;

    private String branchName;
    private Long branchId;
}
