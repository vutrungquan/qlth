package org.example.qlth1.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.dto.request.*;
import org.example.qlth1.dto.response.*;
import org.example.qlth1.entity.*;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {

    TeacherRepository teacherRepository;
    SubjectRepository subjectRepository;
    SchoolClassRepository schoolClassRepository;
    UserRepository userRepository;
    RoleRepository roleRepository;
    StudentRepository studentRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public TeacherResponse createTeacher(TeacherCreateRequest request) {
        // Đảm bảo vai trò giáo viên tồn tại
        ensureRoleExists("TEACHER");
    
        // Tạo và lưu user
        User user = User.builder()
                .username(request.getUsername())
                .password(new BCryptPasswordEncoder(10).encode(request.getPassword()))
                .roles(Set.of(roleRepository.findByName("TEACHER")
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                )
                .build();
        userRepository.save(user);
    
        // Tạo và lưu teacher
        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setPhone(request.getPhone());
        teacher.setUser(user); // Liên kết với user
        teacher.setTeacherCode(request.getTeacherCode()); // Gán teacherCode
        teacher.setBirthDate(request.getBirthDate()); // Gán birthDate
    
        teacherRepository.save(teacher);
        return convertToTeacherResponse(teacher);
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public StudentResponse createStudent(StudentCreateRequest request) {
        // Tạo và lưu user
        User user = User.builder()
                .username(request.getUsername())
                .password(new BCryptPasswordEncoder(10).encode(request.getPassword()))
                .roles(Set.of(roleRepository.findByName("STUDENT")
                        .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                )
                .build();
        userRepository.save(user);
        Student student = new Student();
        student.setName(request.getName());
        student.setUser(user); 
        student.setStudentCode(request.getStudentCode()); 
        student.setBirthDate(request.getBirthDate());
 
        SchoolClass schoolClass = schoolClassRepository.findByClassCode(request.getClassCode())
                .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
        student.setSchoolClass(schoolClass); 
    
        studentRepository.save(student);
    
        Set<Subject> subjects = subjectRepository.findAll().stream().collect(Collectors.toSet()); 
        student.setSubjects(subjects); 
        studentRepository.save(student); 
    
        return convertToStudentResponse(student);
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

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse assignRolesToUser(AssignRoleRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Set<Role> roles = new HashSet<>();
        for (String roleName : request.getRoleNames()) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(role);
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        
        log.info("Assigned roles {} to user {}", request.getRoleNames(), request.getUsername());
        return convertToUserResponse(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void assignClassesToTeacher(String teacherName, Set<String> classCodes) {
        Teacher teacher = teacherRepository.findByName(teacherName)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Set<SchoolClass> classes = new HashSet<>();
        for (String classCode : classCodes) {
            SchoolClass schoolClass = schoolClassRepository.findByClassCode(classCode)
                    .orElseThrow(() -> new AppException(ErrorCode.CLASS_NOT_FOUND));
            classes.add(schoolClass);
        }
        teacherRepository.save(teacher);
    }
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void assignSubjectsToTeacher(String teacherName, Set<String> subjectCodes) {
        Teacher teacher = teacherRepository.findByName(teacherName)
                .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
        Set<Subject> subjects = new HashSet<>();
        for (String subjectCode : subjectCodes) {
            Subject subject = subjectRepository.findBySubjectCode(subjectCode)
                    .orElseThrow(() -> new AppException(ErrorCode.SUBJECT_NOT_FOUND));
            subjects.add(subject);
        }
        teacher.setSubjects(subjects); 
        teacherRepository.save(teacher);
    }
    private TeacherResponse convertToTeacherResponse(Teacher teacher) {
        return TeacherResponse.builder()
            .id(teacher.getId())
            .teacherCode(teacher.getTeacherCode())
            .name(teacher.getName())
            .phone(teacher.getPhone())
            .birthDate(teacher.getBirthDate())
            .classes(teacher.getClasses() != null 
                ? teacher.getClasses().stream().map(SchoolClass::getClassCode).collect(Collectors.toSet()) 
                : Collections.emptySet())
            .subjects(teacher.getSubjects() != null 
                ? teacher.getSubjects().stream().map(Subject::getSubjectCode).collect(Collectors.toSet()) 
                : Collections.emptySet())
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
    
    private StudentResponse convertToStudentResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .name(student.getName())
                .birthDate(student.getBirthDate())
                .classCode(student.getSchoolClass() != null ? student.getSchoolClass().getClassCode() : null)
                .build();
    }

    private UserResponse convertToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .roles(user.getRoles().stream()
                        .map(role -> RoleResponse.builder()
                                .name(role.getName())
                                .description(role.getDescription())
                                .permissions(role.getPermissions().stream()
                                        .map(permission -> PermissionResponse.builder()
                                                .name(permission.getName())
                                                .description(permission.getDescription())
                                                .build())
                                        .collect(Collectors.toSet()))
                                .build())
                        .collect(Collectors.toSet()))
                .build();
    }

    private void ensureRoleExists(String roleName) {
        if (!roleRepository.findByName(roleName).isPresent()) {
            Role role = Role.builder()
                    .name(roleName)
                    .description("Vai trò " + roleName)
                    .build();
            roleRepository.save(role);
        }
    }
}