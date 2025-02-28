package org.example.qlth1.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScoreRequest {
    private Long studentId;      
    private String subjectCode;  
    private Double scoreValue;   
}