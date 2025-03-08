package com.project.vaccine.repository;

import com.project.vaccine.dto.response.ChildResponse;
import com.project.vaccine.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {

    Optional<Child> findByIdAndUserId(Long id, Long userId);

    List<Child> findByUserIdAndStatus(Long userId, boolean status);

    @Query("SELECT c FROM Child c WHERE c.name LIKE %:name%")
    List<Child> findByNameContaining(@Param("name") String name);

    @Query("SELECT c FROM Child c JOIN FETCH c.user")
    List<Child> findAllChildByUser();



}