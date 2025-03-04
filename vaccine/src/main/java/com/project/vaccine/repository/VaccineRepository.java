package com.project.vaccine.repository;

import com.project.vaccine.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Vaccine> findByNameIgnoreCase(String name);

    Optional<Vaccine> findByNameIgnoreCaseAndStatus(String name, Boolean status);
    List<Vaccine> findByStatus(Boolean status);
}
