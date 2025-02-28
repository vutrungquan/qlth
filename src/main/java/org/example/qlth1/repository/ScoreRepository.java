package org.example.qlth1.repository;
import java.util.List;
import org.example.qlth1.entity.Score;
import org.example.qlth1.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAllByStudent(Student student);
}
