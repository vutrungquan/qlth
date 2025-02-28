package org.example.qlth1.controller;

import jakarta.validation.Valid;
import org.example.qlth1.dto.request.ScoreRequest;
import org.example.qlth1.dto.request.StudentRequest;
import org.example.qlth1.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.example.qlth1.dto.request.ApiResponse;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TeacherController {
    TeacherService teacherService;

    // Quản lý thông tin Học sinh do giáo viên phụ trách
    @PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @PutMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<String>> updateStudent(@PathVariable Long studentId, @Valid @RequestBody StudentRequest studentRequest) {
        teacherService.updateStudent(studentId, studentRequest);
        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .result("Cập nhật thông tin học sinh thành công")
                .build()
        );
    }

    @PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<String>> deleteStudent(@PathVariable Long studentId) {
        teacherService.deleteStudent(studentId);
        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .result("Xóa học sinh thành công")
                .build()
        );
    }

    // Quản lý Điểm cho Học sinh
    @PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @PostMapping("/score")
    public ResponseEntity<ApiResponse<String>> addScore(@Valid @RequestBody ScoreRequest scoreRequest) {
        teacherService.addScore(scoreRequest);
        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .result("Thêm điểm thành công")
                .build()
        );
    }

    @PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @PutMapping("/score/{scoreId}")
    public ResponseEntity<ApiResponse<String>> updateScore(@PathVariable Long scoreId, @Valid @RequestBody ScoreRequest scoreRequest) {
        teacherService.updateScore(scoreId, scoreRequest);
        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .result("Cập nhật điểm thành công")
                .build()
        );
    }

    @PreAuthorize("hasAuthority('TEACHER_ROLE')")
    @DeleteMapping("/score/{scoreId}")
    public ResponseEntity<ApiResponse<String>> deleteScore(@PathVariable Long scoreId) {
        teacherService.deleteScore(scoreId);
        return ResponseEntity.ok(
            ApiResponse.<String>builder()
                .result("Xóa điểm thành công")
                .build()
        );
    }
}