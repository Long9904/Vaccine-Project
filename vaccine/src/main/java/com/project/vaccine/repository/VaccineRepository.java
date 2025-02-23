package com.project.vaccine.repository;

import com.project.vaccine.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
    Optional<Vaccine> findByName(String name);

    Optional<Vaccine> findByNameIgnoreCase(String name);

    List<Vaccine> findByStatus(Boolean status);
}
