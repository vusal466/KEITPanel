package com.example.keitpanel.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentReturnRequest {

    @Size(max = 50)
    private String returnDoc;

    private String note;


}
