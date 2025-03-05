package org.example.qlth1.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;
@Data
public class StudentCreateRequest {
    @NotBlank(message = "Tên học sinh không được để trống")
    private String name;

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;

    private String studentCode;    

    private LocalDate birthDate;

    private String classCode;
}