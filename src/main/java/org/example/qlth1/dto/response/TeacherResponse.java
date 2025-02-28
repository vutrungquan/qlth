package org.example.qlth1.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherResponse {
    private Long id;
    private String teacherCode;
    private String name;
    private String phone;
    private LocalDate birthDate;
    private Set<String> classes;    
    private Set<String> subjects;  
}