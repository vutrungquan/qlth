package org.example.qlth1.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolClassRequest {
    private String classCode;   
    private String className;
}