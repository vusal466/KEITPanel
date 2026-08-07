package com.example.keitpanel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentUpdateRequest {

    @NotBlank(message = "Şöbə adı boş ola bilməz")
    @Size(max = 150)
    private String name;

    @Size(max = 20)
    private String code;

    private boolean active;

    @NotNull(message = "Filial seçilməlidir")
    private Long branchId;
}
