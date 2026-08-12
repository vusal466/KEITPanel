package com.example.keitpanel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCreateRequest {

    @Size(max = 30)
    private String personnelNo;

    @NotBlank(message = "Ad boş ola bilməz.")
    @Size(max = 80)
    private String firstName;

    @NotBlank(message = "Soyad boş ola bilməz.")
    @Size(max = 80)
    private String lastName;

    @Size(max = 80)
    private String email;

    @Size(max = 80)
    private String patronymic;

    @Size(max = 30)
    private String phoneMobile;

    @Size(max = 20)
    private String phoneInternal;

    @NotBlank(message = "Anydesk Id boş ola bilməz.")
    @Size(max = 30)
    private String anydeskId;

    @NotBlank(message = "Pc adı boş ola bilməz.")
    @Size(max = 50)
    private String pcUserName;

    @NotNull(message = "Filial seçilməlidir")
    private Long branchId;

    @NotNull(message = "vəzifə seçilməlidir.")
    private Long positionId;

    private Long departmentId;
}
