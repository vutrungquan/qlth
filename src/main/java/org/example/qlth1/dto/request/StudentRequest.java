package org.example.qlth1.dto.request;


import lombok.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentRequest {
    private String studentCode;    
    private String name;
    private LocalDate birthDate;
    private String classCode;
}