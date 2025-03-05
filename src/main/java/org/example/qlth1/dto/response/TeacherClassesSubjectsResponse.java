package org.example.qlth1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class TeacherClassesSubjectsResponse {
    private Set<SchoolClassResponse> classes;
    private Set<SubjectResponse> subjects;
}