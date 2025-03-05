package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

import org.example.qlth1.dto.request.ScoreRequest;
import org.example.qlth1.dto.request.StudentRequest;
import org.example.qlth1.dto.response.SchoolClassResponse;
import org.example.qlth1.dto.response.SubjectResponse;
import org.example.qlth1.dto.response.TeacherClassesSubjectsResponse;
import org.example.qlth1.entity.*;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.ScoreRepository;
import org.example.qlth1.repository.StudentRepository;
import org.example.qlth1.repository.TeacherRepository;
import org.example.qlth1.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    @Transactional
    public void updateStudent(Long studentId, StudentRequest request) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        student.setName(request.getName());
        student.setBirthDate(request.getBirthDate());
        studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void addScore(ScoreRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        Score score = Score.builder()
                .student(student)
                .scoreValue(request.getScoreValue())
                .build();
        scoreRepository.save(score);
    }

    @Transactional
    public void updateScore(Long scoreId, ScoreRequest request) {
        Score score = scoreRepository.findById(scoreId)
                .orElseThrow(() -> new AppException(ErrorCode.SCORE_NOT_FOUND));
        score.setScoreValue(request.getScoreValue());
        scoreRepository.save(score);
    }

    @Transactional
    public void deleteScore(Long scoreId) {
        scoreRepository.deleteById(scoreId);
    }
    @Transactional(readOnly = true)
    public TeacherClassesSubjectsResponse getClassesAndSubjects() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Teacher teacher = teacherRepository.findByUser(currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Set<SchoolClassResponse> classes = teacher.getClasses().stream()
                .map(this::convertToSchoolClassResponse)
                .collect(Collectors.toSet());
        Set<SubjectResponse> subjects = teacher.getSubjects().stream()
                .map(this::convertToSubjectResponse)
                .collect(Collectors.toSet());
        
        return TeacherClassesSubjectsResponse.builder()
                .classes(classes)
                .subjects(subjects)
                .build();
    }

    private SchoolClassResponse convertToSchoolClassResponse(SchoolClass schoolClass) {
        return SchoolClassResponse.builder()
                .classCode(schoolClass.getClassCode())
                .className(schoolClass.getClassName())
                .build();
    }

    private SubjectResponse convertToSubjectResponse(Subject subject) {
        return SubjectResponse.builder()
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .build();
    }
}

