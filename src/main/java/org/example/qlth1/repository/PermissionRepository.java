package org.example.qlth1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.qlth1.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {}
