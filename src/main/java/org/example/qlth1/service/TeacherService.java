package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.dto.request.ScoreRequest;
import org.example.qlth1.dto.request.StudentRequest;
import org.example.qlth1.entity.Score;
import org.example.qlth1.entity.Student;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.ScoreRepository;
import org.example.qlth1.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherService {

    private final StudentRepository studentRepository;
    private final ScoreRepository scoreRepository;


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
}
