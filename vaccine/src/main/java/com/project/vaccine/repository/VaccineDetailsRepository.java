package com.project.vaccine.repository;

import com.project.vaccine.entity.VaccineDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VaccineDetailsRepository extends JpaRepository<VaccineDetails, Long> {


    Optional<VaccineDetails> findByIdAndVaccineId(Long id, Long vaccineId);

    List<VaccineDetails> findByVaccineId(Long vaccineId);

}
