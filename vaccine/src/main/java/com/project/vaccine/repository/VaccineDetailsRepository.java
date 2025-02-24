package com.project.vaccine.repository;

import com.project.vaccine.entity.VaccineDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineDetailsRepository extends JpaRepository<VaccineDetails, Long> {

}
