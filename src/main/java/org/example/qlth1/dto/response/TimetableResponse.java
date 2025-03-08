package org.example.qlth1.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class TimetableResponse {
    private String subjectName; // Tên môn học
    private String teacherName; // Tên giáo viên
    private String classCode; // Mã lớp học
    private DayOfWeek day; // Ngày trong tuần
    private LocalTime startTime; // Thời gian bắt đầu
    private LocalTime endTime; // Thời gian kết thúc
}