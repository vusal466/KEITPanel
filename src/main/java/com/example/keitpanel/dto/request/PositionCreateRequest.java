package com.example.keitpanel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionCreateRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @Size(max = 20)
    private String code;

    @Positive(message = "Ştat sayı müsbət olmalıdır")
    private short headCount;

    @NotNull(message = "Filial seçilməlidir")
    private Long branchId;

    private Long departmentId;

    private Long reportsToId;

}
