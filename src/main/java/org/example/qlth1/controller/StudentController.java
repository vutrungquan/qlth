package org.example.qlth1.controller;

import org.example.qlth1.service.StudentService;
import org.example.qlth1.dto.request.ApiResponse;
import org.example.qlth1.dto.response.StudentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentController {
    StudentService studentService;

    @PreAuthorize("hasAuthority('STUDENT_ROLE')")
    @GetMapping("/score")
    public ResponseEntity<ApiResponse<List<StudentResponse>>> viewScores() {
        var scores = studentService.getAllStudents();
        return ResponseEntity.ok(
            ApiResponse.<List<StudentResponse>>builder()
                .result(scores)
                .build()
        );
    }
}