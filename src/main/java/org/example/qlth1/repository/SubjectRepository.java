package org.example.qlth1.repository;

import org.example.qlth1.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, String> {
    boolean existsBySubjectCode(String subjectCode);
}
