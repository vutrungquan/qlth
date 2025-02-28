package org.example.qlth1.repository;


import org.example.qlth1.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByTeacherCode(String teacherCode);
}