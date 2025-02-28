package org.example.qlth1.repository;

import org.example.qlth1.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass, String> {
    boolean existsByClassCode(String classCode);
}