package org.example.qlth1.controller;

import org.example.qlth1.dto.response.SchoolClassResponse;
import org.example.qlth1.dto.response.ScoreResponse;
import org.example.qlth1.service.StudentService;
import org.example.qlth1.dto.request.ApiResponse;
import org.example.qlth1.dto.response.StudentResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
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

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/info")
    public ApiResponse<StudentResponse> getMyInfo() {
        StudentResponse studentInfo = studentService.getMyInfo();
        return ApiResponse.<StudentResponse>builder()
                .code(HttpStatus.OK.value())
                .result(studentInfo)
                .message("Lấy thông tin cá nhân thành công")
                .build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/class")
    public ApiResponse<SchoolClassResponse> getStudentClass() {
        SchoolClassResponse classResponse = studentService.getStudentClass();
        return ApiResponse.<SchoolClassResponse>builder()
                .code(HttpStatus.OK.value())
                .result(classResponse)
                .message("Lấy thông tin lớp học thành công")
                .build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/scores")
    public ApiResponse<List<ScoreResponse>> getStudentScores() {
        List<ScoreResponse> scores = studentService.getMyScores();
        return ApiResponse.<List<ScoreResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(scores)
                .message("Lấy danh sách điểm số thành công")
                .build();
    }
}