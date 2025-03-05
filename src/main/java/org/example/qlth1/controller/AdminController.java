package org.example.qlth1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.qlth1.dto.request.*;
import org.example.qlth1.dto.response.*;
import org.example.qlth1.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/teacher")
    public ApiResponse<TeacherResponse> createTeacher(@Valid @RequestBody TeacherCreateRequest teacherRequest) {
        return ApiResponse.<TeacherResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(adminService.createTeacher(teacherRequest))
                .message("Tạo giáo viên thành công")
                .build();
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/student")
    public ApiResponse<StudentResponse> createStudent(@Valid @RequestBody StudentCreateRequest studentRequest) {
        return ApiResponse.<StudentResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(adminService.createStudent(studentRequest))
                .message("Tạo học sinh thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/teacher/{id}")
    public ApiResponse<TeacherResponse> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherRequest teacherRequest) {
        return ApiResponse.<TeacherResponse>builder()
                .code(HttpStatus.OK.value())
                .result(adminService.updateTeacher(id, teacherRequest))
                .message("Cập nhật giáo viên thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/teacher/{id}")
    public ApiResponse<String> deleteTeacher(@PathVariable Long id) {
        adminService.deleteTeacher(id);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Xóa giáo viên thành công")
                .build();
    }

    // Quản lý Môn học
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/subject")
    public ApiResponse<SubjectResponse> createSubject(@Valid @RequestBody SubjectRequest subjectRequest) {
        return ApiResponse.<SubjectResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(adminService.createSubject(subjectRequest))
                .message("Tạo môn học thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/subject/{subjectCode}")
    public ApiResponse<SubjectResponse> updateSubject(@PathVariable String subjectCode, @Valid @RequestBody SubjectRequest subjectRequest) {
        return ApiResponse.<SubjectResponse>builder()
                .code(HttpStatus.OK.value())
                .result(adminService.updateSubject(subjectCode, subjectRequest))
                .message("Cập nhật môn học thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/subject/{subjectCode}")
    public ApiResponse<String> deleteSubject(@PathVariable String subjectCode) {
        adminService.deleteSubject(subjectCode);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Xóa môn học thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/class")
    public ApiResponse<SchoolClassResponse> createSchoolClass(@Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        return ApiResponse.<SchoolClassResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(adminService.createSchoolClass(schoolClassRequest))
                .message("Tạo lớp học thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/class/{classCode}")
    public ApiResponse<SchoolClassResponse> updateSchoolClass(@PathVariable String classCode, @Valid @RequestBody SchoolClassRequest schoolClassRequest) {
        return ApiResponse.<SchoolClassResponse>builder()
                .code(HttpStatus.OK.value())
                .result(adminService.updateSchoolClass(classCode, schoolClassRequest))
                .message("Cập nhật lớp học thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/class/{classCode}")
    public ApiResponse<String> deleteSchoolClass(@PathVariable String classCode) {
        adminService.deleteSchoolClass(classCode);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Xóa lớp học thành công")
                .build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/assignroles")
    public ApiResponse<UserResponse> assignRolesToUser(@Valid @RequestBody AssignRoleRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(adminService.assignRolesToUser(request))
                .message("Gán vai trò cho người dùng thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(adminService.getAllUsers())
                .message("Lấy danh sách người dùng thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/teacher/assign-classes")
    public ApiResponse<Void> assignClassesToTeacher(@RequestParam String teacherName, @RequestBody Set<String> classCodes) {
        adminService.assignClassesToTeacher(teacherName, classCodes);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Gán lớp học cho giáo viên thành công")
                .build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/teacher/assign-subjects")
    public ApiResponse<Void> assignSubjectsToTeacher(@RequestParam String teacherName, @RequestBody Set<String> subjectCodes) {
        adminService.assignSubjectsToTeacher(teacherName, subjectCodes);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Gán môn học cho giáo viên thành công")
                .build();
    }
}
