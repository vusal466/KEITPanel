package com.example.keitpanel.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchResponse {
    private Long id;
    private String code;
    private String name;
    private String address;
    private String city;
    private String phone;
    private boolean active;

    private Long parentId;
    private String parentName;
}
