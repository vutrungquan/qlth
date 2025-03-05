package org.example.qlth1.controller;

import jakarta.validation.Valid;
import org.example.qlth1.dto.request.ScoreRequest;
import org.example.qlth1.dto.request.StudentRequest;
import org.example.qlth1.dto.response.TeacherClassesSubjectsResponse;
import org.example.qlth1.service.TeacherService;
import org.springframework.http.HttpStatus;
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
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/student/{studentId}")
    public ApiResponse<String> updateStudent(@PathVariable Long studentId, @Valid @RequestBody StudentRequest studentRequest) {
        teacherService.updateStudent(studentId, studentRequest);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Cập nhật thông tin học sinh thành công")
                .build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/student/{studentId}")
    public ApiResponse<String> deleteStudent(@PathVariable Long studentId) {
        teacherService.deleteStudent(studentId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Xóa học sinh thành công")
                .build();
    }

    // Quản lý Điểm cho Học sinh
    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/score")
    public ApiResponse<String> addScore(@Valid @RequestBody ScoreRequest scoreRequest) {
        teacherService.addScore(scoreRequest);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Thêm điểm thành công")
                .build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/score/{scoreId}")
    public ApiResponse<String> updateScore(@PathVariable Long scoreId, @Valid @RequestBody ScoreRequest scoreRequest) {
        teacherService.updateScore(scoreId, scoreRequest);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Cập nhật điểm thành công")
                .build();
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/score/{scoreId}")
    public ApiResponse<String> deleteScore(@PathVariable Long scoreId) {
        teacherService.deleteScore(scoreId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Xóa điểm thành công")
                .build();
    }
   @PreAuthorize("hasRole('TEACHER')")
   @GetMapping("/classessubjects")
   public ApiResponse<TeacherClassesSubjectsResponse> getClassesAndSubjects() {
       TeacherClassesSubjectsResponse response = teacherService.getClassesAndSubjects();
       return ApiResponse.<TeacherClassesSubjectsResponse>builder()
                .code(HttpStatus.OK.value())
                .result(response)
                .message("Lấy danh sách lớp học và môn học thành công")
                .build();
   }
}
