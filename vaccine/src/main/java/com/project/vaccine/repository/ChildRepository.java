package com.project.vaccine.repository;

import com.project.vaccine.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    Optional<Child> findByIdAndUserId(Long id, Long userId);

    List<Child> findByUserIdAndStatus(Long userId, boolean status);
}