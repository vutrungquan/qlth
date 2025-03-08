package org.example.qlth1.controller;

import lombok.RequiredArgsConstructor;
import org.example.qlth1.dto.response.TimetableResponse;
import org.example.qlth1.service.TimetableService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
public class TimetableController {
    private final TimetableService timetableService;

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping("/teacher/my-timetable")
    public List<TimetableResponse> getMyTimetableForTeacher() {
        return timetableService.getTimetableForCurrentTeacher();
    }
    
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student/my-timetable")
    public List<TimetableResponse> getMyTimetableForStudent() {
        return timetableService.getTimetableForCurrentStudent(); // Tương tự cho học sinh
    }
}