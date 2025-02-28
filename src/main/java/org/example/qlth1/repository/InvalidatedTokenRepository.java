package org.example.qlth1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.qlth1.entity.InvalidatedToken;
@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
