package com.example.keitpanel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchCreateRequest {

    @NotBlank(message = "Filial kodu boş ola bilməz")
    @Size(max = 20, message = "Kodu maksimum 20 simvol ola bilər")
    private String code;

    @NotBlank(message = "Filial adı boş ola bilməz")
    @Size(max = 150)
    private String name;

    private String address;
    private String city;
    private String phone;

    private Long parentId;
}
