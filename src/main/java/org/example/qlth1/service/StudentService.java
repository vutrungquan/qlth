package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.dto.response.SchoolClassResponse;
import org.example.qlth1.dto.response.ScoreResponse;
import org.example.qlth1.dto.response.StudentResponse;
import org.example.qlth1.entity.SchoolClass;
import org.example.qlth1.entity.Student;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.StudentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.example.qlth1.repository.ScoreRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class StudentService {
    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    public StudentResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return convertToStudentResponse(student);
    }

    public SchoolClassResponse getStudentClass() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return convertToSchoolClassResponse(student.getSchoolClass());
    }

    public List<ScoreResponse> getMyScores() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Student student = studentRepository.findByName(username)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        return scoreRepository.findByStudentId(student.getId()); // Lấy điểm của học sinh
    }

    private SchoolClassResponse convertToSchoolClassResponse(SchoolClass schoolClass) {
        return SchoolClassResponse.builder()
                .classCode(schoolClass.getClassCode())
                .className(schoolClass.getClassName())
                .build();
    }

    private StudentResponse convertToStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .studentCode(student.getStudentCode())
                .birthDate(student.getBirthDate())
                .build();
    }
}
