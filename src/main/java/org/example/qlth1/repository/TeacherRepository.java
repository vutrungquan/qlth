package org.example.qlth1.repository;


import org.example.qlth1.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.qlth1.entity.User;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    Optional<Teacher> findByName(String name);
    Optional<Teacher> findByUser(User user);
}