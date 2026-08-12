package com.example.keitpanel.dto.response;

import com.example.keitpanel.entities.employee.EmployeeStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeResponse {

    private Long id;

    private String personnelNo;

    private String firstName;

    private String lastName;

    private String patronymic;

    private EmployeeStatus status;

    private String phoneMobile;

    private String email;

    private String phoneInternal;

    private String anydeskId;

    private String pcUsername;

    private Long branchId;

    private String branchName;

    private Long positionId;

    private String positionName;

    private Long departmentId;

    private LocalDate hiredAt;
    private LocalDate terminatedAt;

    private String departmentName;
}
