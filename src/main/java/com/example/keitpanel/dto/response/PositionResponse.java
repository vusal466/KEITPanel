package com.example.keitpanel.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionResponse {

    private Long id;
    private String title;
    private String code;
    private short headCount;
    private boolean active;

    private Long branchId;
    private String branchName;

    private Long departmentId;
    private String departmentName;

    private Long reportsToId;
    private String reportsToTitle;
}
