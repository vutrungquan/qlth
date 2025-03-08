package org.example.qlth1.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "timetable")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // Giáo viên dạy

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject; // Môn học

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass; // Lớp học

    private DayOfWeek day; // Ngày trong tuần
    private LocalTime startTime; // Thời gian bắt đầu
    private LocalTime endTime; // Thời gian kết thúc
}