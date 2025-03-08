package org.example.qlth1.service;

import lombok.RequiredArgsConstructor;
import org.example.qlth1.entity.Timetable;
import org.example.qlth1.entity.Teacher;
import org.example.qlth1.entity.Subject;
import org.example.qlth1.entity.SchoolClass;
import org.example.qlth1.repository.TimetableRepository;
import org.example.qlth1.repository.TeacherRepository;
import org.example.qlth1.repository.SubjectRepository;
import org.example.qlth1.repository.SchoolClassRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DataInitializer {
    private final TimetableRepository timetableRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final SchoolClassRepository schoolClassRepository;

    @PostConstruct
    public void init() {
        // Lấy giáo viên, môn học và lớp học từ cơ sở dữ liệu
        Teacher teacher = teacherRepository.findById(1L).orElseThrow(); // Giả sử ID 1 là giáo viên
        Subject subject = subjectRepository.findBySubjectCode("T001").orElseThrow(); // Môn Toán
        SchoolClass schoolClass = schoolClassRepository.findByClassCode("1").orElseThrow(); // Lớp 1

        // Tạo thời khóa biểu
        Timetable timetable1 = Timetable.builder()
                .teacher(teacher)
                .subject(subject)
                .schoolClass(schoolClass)
                .day(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(8, 0)) // 08:00
                .endTime(LocalTime.of(9, 30)) // 09:30
                .build();

        Timetable timetable2 = Timetable.builder()
                .teacher(teacher)
                .subject(subject)
                .schoolClass(schoolClass)
                .day(DayOfWeek.WEDNESDAY)
                .startTime(LocalTime.of(10, 0)) // 10:00
                .endTime(LocalTime.of(11, 30)) // 11:30
                .build();

        // Lưu thời khóa biểu vào cơ sở dữ liệu
        timetableRepository.save(timetable1);
        timetableRepository.save(timetable2);
    }
}