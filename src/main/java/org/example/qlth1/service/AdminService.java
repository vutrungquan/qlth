package org.example.qlth1.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.dto.request.SchoolClassRequest;
import org.example.qlth1.dto.request.SubjectRequest;
import org.example.qlth1.dto.request.TeacherRequest;
import org.example.qlth1.dto.response.SchoolClassResponse;
import org.example.qlth1.dto.response.SubjectResponse;
import org.example.qlth1.dto.response.TeacherResponse;
import org.example.qlth1.entity.SchoolClass;
import org.example.qlth1.entity.Subject;
import org.example.qlth1.entity.Teacher;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.SchoolClassRepository;
import org.example.qlth1.repository.SubjectRepository;
import org.example.qlth1.repository.TeacherRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {

    TeacherRepository teacherRepository;
    SubjectRepository subjectRepository;
    SchoolClassRepository schoolClassRepository;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponse createTeacher(TeacherRequest teacherRequest) {
        if (teacherRepository.existsByTeacherCode(teacherRequest.getTeacherCode())) {
            throw new AppException(ErrorCode.TEACHER_EXISTED);
        }

        Teacher teacher = new Teacher();
        teacher.setTeacherCode(teacherRequest.getTeacherCode());
        teacher.setName(teacherRequest.getName());
        teacher.setPhone(teacherRequest.getPhone());
        teacher.setBirthDate(teacherRequest.getBirthDate());
        teacher.setClasses(teacherRequest.getClassCodes().stream()
                .map(classCode -> schoolClassRepository.findById(classCode)
                        .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND)))
                .collect(Collectors.toSet()));
        teacher.setSubjects(teacherRequest.getSubjectCodes().stream()
                .map(subjectCode -> subjectRepository.findById(subjectCode)
                        .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND)))
                .collect(Collectors.toSet()));

        return convertToTeacherResponse(teacherRepository.save(teacher));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponse updateTeacher(Long id, TeacherRequest teacherRequest) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

        teacher.setTeacherCode(teacherRequest.getTeacherCode());
        teacher.setName(teacherRequest.getName());
        teacher.setPhone(teacherRequest.getPhone());
        teacher.setBirthDate(teacherRequest.getBirthDate());
        teacher.setClasses(teacherRequest.getClassCodes().stream()
                .map(classCode -> schoolClassRepository.findById(classCode)
                        .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND)))
                .collect(Collectors.toSet()));
        teacher.setSubjects(teacherRequest.getSubjectCodes().stream()
                .map(subjectCode -> subjectRepository.findById(subjectCode)
                        .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND)))
                .collect(Collectors.toSet()));

        return convertToTeacherResponse(teacherRepository.save(teacher));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new AppException(ErrorCode.TEACHER_NOT_FOUND);
        }
        teacherRepository.deleteById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findAll().stream()
                .map(this::convertToTeacherResponse)
                .collect(Collectors.toList());
    }

 
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SubjectResponse createSubject(SubjectRequest subjectRequest) {
        if (subjectRepository.existsBySubjectCode(subjectRequest.getSubjectCode())) {
            throw new AppException(ErrorCode.SUBJECT_EXISTED);
        }

        Subject subject = new Subject();
        subject.setSubjectCode(subjectRequest.getSubjectCode());
        subject.setSubjectName(subjectRequest.getSubjectName());

        return convertToSubjectResponse(subjectRepository.save(subject));
    }

 
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SubjectResponse updateSubject(String subjectCode, SubjectRequest subjectRequest) {
        Subject subject = subjectRepository.findById(subjectCode)
                .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));

        subject.setSubjectName(subjectRequest.getSubjectName());

        return convertToSubjectResponse(subjectRepository.save(subject));
    }

  
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSubject(String subjectCode) {
        if (!subjectRepository.existsById(subjectCode)) {
            throw new AppException(ErrorCode.SUBJECT_NOT_FOUND);
        }
        subjectRepository.deleteById(subjectCode);
    }

 
    @PreAuthorize("hasRole('ADMIN')")
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::convertToSubjectResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SchoolClassResponse createSchoolClass(SchoolClassRequest schoolClassRequest) {
        if (schoolClassRepository.existsByClassCode(schoolClassRequest.getClassCode())) {
            throw new AppException(ErrorCode.CLASS_EXISTED);
        }

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassCode(schoolClassRequest.getClassCode());
        schoolClass.setClassName(schoolClassRequest.getClassName());

        return convertToSchoolClassResponse(schoolClassRepository.save(schoolClass));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public SchoolClassResponse updateSchoolClass(String classCode, SchoolClassRequest schoolClassRequest) {
        SchoolClass schoolClass = schoolClassRepository.findById(classCode)
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));

        schoolClass.setClassName(schoolClassRequest.getClassName());

        return convertToSchoolClassResponse(schoolClassRepository.save(schoolClass));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSchoolClass(String classCode) {
        if (!schoolClassRepository.existsById(classCode)) {
            throw new AppException(ErrorCode.CLASS_NOT_FOUND);
        }
        schoolClassRepository.deleteById(classCode);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public List<SchoolClassResponse> getAllSchoolClasses() {
        return schoolClassRepository.findAll().stream()
                .map(this::convertToSchoolClassResponse)
                .collect(Collectors.toList());
    }

    private TeacherResponse convertToTeacherResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .teacherCode(teacher.getTeacherCode())
                .name(teacher.getName())
                .phone(teacher.getPhone())
                .birthDate(teacher.getBirthDate())
                .classes(teacher.getClasses().stream().map(SchoolClass::getClassCode).collect(Collectors.toSet()))
                .subjects(teacher.getSubjects().stream().map(Subject::getSubjectCode).collect(Collectors.toSet()))
                .build();
    }

    private SubjectResponse convertToSubjectResponse(Subject subject) {
        return SubjectResponse.builder()
                .subjectCode(subject.getSubjectCode())
                .subjectName(subject.getSubjectName())
                .build();
    }

    private SchoolClassResponse convertToSchoolClassResponse(SchoolClass schoolClass) {
        return SchoolClassResponse.builder()
                .classCode(schoolClass.getClassCode())
                .className(schoolClass.getClassName())
                .build();
    }
}