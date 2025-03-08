package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import org.example.qlth1.dto.response.TimetableResponse;
import org.example.qlth1.entity.*;
import org.example.qlth1.exception.AppException;
import org.example.qlth1.exception.ErrorCode;
import org.example.qlth1.repository.StudentRepository;
import org.example.qlth1.repository.TimetableRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.qlth1.repository.UserRepository;
import org.example.qlth1.repository.TeacherRepository;
import org.example.qlth1.repository.SchoolClassRepository;
import org.example.qlth1.repository.SubjectRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final SubjectRepository subjectRepository;
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
    /**
     * Tạo dữ liệu thời khóa biểu tự động cho nhiều tuần
     * @param weeks Số tuần cần tạo thời khóa biểu
     */
    @Transactional
    public void generateTimetableData(int weeks) {
        // Lấy tất cả giáo viên, lớp học và môn học từ cơ sở dữ liệu
        List<Teacher> teachers = teacherRepository.findAll();
        List<SchoolClass> classes = schoolClassRepository.findAll();
        List<Subject> subjects = subjectRepository.findAll();
        
        if (teachers.isEmpty() || classes.isEmpty() || subjects.isEmpty()) {
            throw new AppException(ErrorCode.DATA_NOT_FOUND);
        }
        
        // Xóa tất cả thời khóa biểu hiện có (tùy chọn)
         timetableRepository.deleteAll();
        
        // Đặt ngày bắt đầu là ngày đầu tiên của tuần hiện tại
        LocalDate startDate = LocalDate.now();
        startDate = startDate.minusDays(startDate.getDayOfWeek().getValue() - 1); // Đặt về thứ 2
        
        // Tạo thời khóa biểu cho số tuần được chỉ định
        for (int week = 0; week < weeks; week++) {
            LocalDate weekStart = startDate.plusWeeks(week);
            
            // Tạo thời khóa biểu cho mỗi ngày trong tuần (Thứ 2 đến Thứ 6)
            for (int dayOfWeek = 1; dayOfWeek <= 5; dayOfWeek++) {
                DayOfWeek day = DayOfWeek.of(dayOfWeek);
                
                // Các khung giờ học trong ngày
                LocalTime[][] timeSlots = {
                    {LocalTime.of(7, 30), LocalTime.of(9, 0)},
                    {LocalTime.of(9, 15), LocalTime.of(10, 45)},
                    {LocalTime.of(11, 0), LocalTime.of(12, 30)},
                    {LocalTime.of(13, 30), LocalTime.of(15, 0)},
                    {LocalTime.of(15, 15), LocalTime.of(16, 45)}
                };
                
                for (SchoolClass schoolClass : classes) {
                    // Mỗi lớp có 3 tiết học mỗi ngày
                    for (int slot = 0; slot < 3; slot++) {
                        // Chọn ngẫu nhiên giáo viên và môn học
                        Teacher teacher = getRandomTeacher(teachers);
                        Subject subject = getRandomSubject(subjects, teacher);
                        
                        // Kiểm tra giáo viên có dạy môn học này và lớp này không
                        if (teacher.getSubjects().contains(subject) && teacher.getClasses().contains(schoolClass)) {
                            // Tạo thời khóa biểu mới
                            Timetable timetable = Timetable.builder()
                                    .teacher(teacher)
                                    .subject(subject)
                                    .schoolClass(schoolClass)
                                    .day(day)
                                    .startTime(timeSlots[slot][0])
                                    .endTime(timeSlots[slot][1])
                                    .build();
                            
                            timetableRepository.save(timetable);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Lấy ngẫu nhiên một giáo viên từ danh sách
     */
    private Teacher getRandomTeacher(List<Teacher> teachers) {
        return teachers.get(new Random().nextInt(teachers.size()));
    }
    
    /**
     * Lấy ngẫu nhiên một môn học từ danh sách môn học mà giáo viên có thể dạy
     */
    private Subject getRandomSubject(List<Subject> subjects, Teacher teacher) {
        List<Subject> teacherSubjects = subjects.stream()
                .filter(subject -> teacher.getSubjects().contains(subject))
                .collect(Collectors.toList());
        
        if (teacherSubjects.isEmpty()) {
            // Nếu giáo viên không có môn học nào, lấy ngẫu nhiên một môn
            return subjects.get(new Random().nextInt(subjects.size()));
        } else {
            // Lấy ngẫu nhiên một môn học từ các môn mà giáo viên dạy
            return teacherSubjects.get(new Random().nextInt(teacherSubjects.size()));
        }
    }
}
