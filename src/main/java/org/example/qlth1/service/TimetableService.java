package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import org.example.qlth1.dto.response.TimetableResponse;
import org.example.qlth1.entity.Student;
import org.example.qlth1.entity.Teacher;
import org.example.qlth1.entity.Timetable;
import org.example.qlth1.entity.User;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.StudentRepository;
import org.example.qlth1.repository.TimetableRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.qlth1.repository.UserRepository;
import org.example.qlth1.repository.TeacherRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    public List<TimetableResponse> getTimetableForTeacher(Long teacherId) {
        List<Timetable> timetables = timetableRepository.findByTeacherId(teacherId);
        return timetables.stream()
                .map(this::convertToTimetableResponse)
                .collect(Collectors.toList());
    }

    public List<TimetableResponse> getTimetableForClass(Long classId) {
        List<Timetable> timetables = timetableRepository.findBySchoolClassClassCode(classId.toString());
        return timetables.stream()
                .map(this::convertToTimetableResponse)
                .collect(Collectors.toList());
    }

    private TimetableResponse convertToTimetableResponse(Timetable timetable) {
        return TimetableResponse.builder()
                .subjectName(timetable.getSubject().getSubjectName())
                .teacherName(timetable.getTeacher().getName())
                .classCode(timetable.getSchoolClass().getClassCode())
                .day(timetable.getDay())
                .startTime(timetable.getStartTime())
                .endTime(timetable.getEndTime())
                .build();
    }
    @Transactional(readOnly = true)
public List<TimetableResponse> getTimetableForCurrentTeacher() {
    // Lấy username của người dùng đang đăng nhập
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
    // Tìm user dựa trên username
    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    
    // Tìm teacher liên kết với user này
    Teacher teacher = teacherRepository.findByUser(currentUser)
            .orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
    
    return getTimetableForTeacher(teacher.getId()); // Gọi phương thức lấy thời khóa biểu
}
@Transactional(readOnly = true)
public List<TimetableResponse> getTimetableForCurrentStudent() {
    // Lấy username của người dùng đang đăng nhập
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    
    // Tìm user dựa trên username
    User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    
    // Tìm student liên kết với user này
    Student student = studentRepository.findByUser(currentUser)
            .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
    
    return getTimetableForClass(Long.valueOf(student.getSchoolClass().getClassCode())); // Gọi phương thức lấy thời khóa biểu theo lớp
}
}