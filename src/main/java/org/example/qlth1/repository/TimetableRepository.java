package org.example.qlth1.repository;


import org.example.qlth1.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByTeacherId(Long teacherId); // Lấy thời khóa biểu theo giáo viên
    List<Timetable> findBySchoolClassClassCode(String classId); // Lấy thời khóa biểu theo lớp học
}