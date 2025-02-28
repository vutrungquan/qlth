package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.dto.response.StudentResponse;
import org.example.qlth1.entity.Student;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

  
    public StudentResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByStudentCode(username)
            .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return convertToStudentResponse(student);
    }


    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToStudentResponse)
                .toList();
    }

    private StudentResponse convertToStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .name(student.getName())
                .birthDate(student.getBirthDate())
                .build();
    }
}