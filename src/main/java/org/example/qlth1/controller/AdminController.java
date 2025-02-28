package org.example.qlth1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.qlth1.dto.request.ApiResponse;
import org.example.qlth1.dto.request.SchoolClassRequest;
import org.example.qlth1.dto.request.SubjectRequest;
import org.example.qlth1.dto.request.TeacherRequest;
import org.example.qlth1.dto.response.TeacherResponse;
import org.example.qlth1.dto.response.SubjectResponse;
import org.example.qlth1.dto.response.SchoolClassResponse;
import org.example.qlth1.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Quản lý Giáo viên
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PostMapping("/teacher")
    public ResponseEntity<ApiResponse<TeacherResponse>> createTeacher(@Valid @RequestBody TeacherRequest teacherRequest) {
        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .result(adminService.createTeacher(teacherRequest))
                        .message("Tạo giáo viên thành công")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PutMapping("/teacher/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequest teacherRequest) {
        return ResponseEntity.ok(
                ApiResponse.<TeacherResponse>builder()
                        .result(adminService.updateTeacher(id, teacherRequest))
                        .message("Cập nhật giáo viên thành công")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
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
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PostMapping("/subject")
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SubjectResponse>builder()
                        .result(adminService.createSubject(subjectRequest))
                        .message("Tạo môn học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PutMapping("/subject/{subjectCode}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(@PathVariable String subjectCode, @Valid @RequestBody SubjectRequest subjectRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SubjectResponse>builder()
                        .result(adminService.updateSubject(subjectCode, subjectRequest))
                        .message("Cập nhật môn học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @DeleteMapping("/subject/{subjectCode}")
    public ResponseEntity<ApiResponse<String>> deleteSubject(@PathVariable String subjectCode) {
        adminService.deleteSubject(subjectCode);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result("Xóa môn học thành công")
                        .build()
        );
    }

    // Quản lý Lớp học
    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PostMapping("/class")
    public ResponseEntity<ApiResponse<SchoolClassResponse>> createSchoolClass(@Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SchoolClassResponse>builder()
                        .result(adminService.createSchoolClass(schoolClassRequest))
                        .message("Tạo lớp học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @PutMapping("/class/{classCode}")
    public ResponseEntity<ApiResponse<SchoolClassResponse>> updateSchoolClass(@PathVariable String classCode, @Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        return ResponseEntity.ok(
                ApiResponse.<SchoolClassResponse>builder()
                        .result(adminService.updateSchoolClass(classCode, schoolClassRequest))
                        .message("Cập nhật lớp học thành công")
                        .build()
        );
    }

    @PreAuthorize("hasAuthority('ADMIN_ROLE')")
    @DeleteMapping("/class/{classCode}")
    public ResponseEntity<ApiResponse<String>> deleteSchoolClass(@PathVariable String classCode) {
        adminService.deleteSchoolClass(classCode);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .result("Xóa lớp học thành công")
                        .build()
        );
    }
}
