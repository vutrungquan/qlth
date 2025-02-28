package org.example.qlth1.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class TeacherRequest {
    private String teacherCode;    
    private String name;
    private String phone;
    private LocalDate birthDate;
    private Set<String> classCodes;
    private Set<String> subjectCodes;
}
