package org.example.qlth1.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectRequest {
    private String subjectCode;  
    private String subjectName;
}