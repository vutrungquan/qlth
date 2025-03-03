package org.example.qlth1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.qlth1.dto.request.*;
import org.example.qlth1.dto.response.TeacherResponse;
import org.example.qlth1.dto.response.SubjectResponse;
import org.example.qlth1.dto.response.SchoolClassResponse;
import org.example.qlth1.dto.response.UserResponse;
import org.example.qlth1.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teacher")
    public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(@Valid @RequestBody TeacherRequest teacherRequest) {
        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .result(adminService.createTeacher(teacherRequest))
                        .message("Tạo giáo viên thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/teacher/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequest teacherRequest) {
        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .result(adminService.updateTeacher(id, teacherRequest))
                        .message("Cập nhật giáo viên thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<ApiResponse<String>> deleteTeacher(@PathVariable Long id) {
        adminService.deleteTeacher(id);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result("Xóa giáo viên thành công")
                        .build()
        );
    }

    // Quản lý Môn học
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/subject")
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SubjectResponse>builder()
                        .result(adminService.createSubject(subjectRequest))
                        .message("Tạo môn học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/subject/{subjectCode}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(@PathVariable String subjectCode, @Valid @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SubjectResponse>builder()
                        .result(adminService.updateSubject(subjectCode, subjectRequest))
                        .message("Cập nhật môn học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/subject/{subjectCode}")
    public ResponseEntity<ApiResponse<String>> deleteSubject(@PathVariable String subjectCode) {
        adminService.deleteSubject(subjectCode);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result("Xóa môn học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/class")
    public ResponseEntity<ApiResponse<SchoolClassResponse>> createSchoolClass(@Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SchoolClassResponse>builder()
                        .result(adminService.createSchoolClass(schoolClassRequest))
                        .message("Tạo lớp học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/class/{classCode}")
    public ResponseEntity<ApiResponse<SchoolClassResponse>> updateSchoolClass(@PathVariable String classCode, @Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SchoolClassResponse>builder()
                        .result(adminService.updateSchoolClass(classCode, schoolClassRequest))
                        .message("Cập nhật lớp học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/class/{classCode}")
    public ResponseEntity<ApiResponse<String>> deleteSchoolClass(@PathVariable String classCode) {
        adminService.deleteSchoolClass(classCode);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result("Xóa lớp học thành công")
                        .build()
        );
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/assignroles")
    public ResponseEntity<ApiResponse<UserResponse>> assignRolesToUser(
            @Valid @RequestBody AssignRoleRequest request) {
        return ResponseEntity.ok(
                ApiResponse.<UserResponse>builder()
                        .result(adminService.assignRolesToUser(request))
                        .message("Gán vai trò cho người dùng thành công")
                        .build()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(
                ApiResponse.<List<UserResponse>>builder()
                        .result(adminService.getAllUsers())
                        .message("Lấy danh sách người dùng thành công")
                        .build()
        );
    }
}
