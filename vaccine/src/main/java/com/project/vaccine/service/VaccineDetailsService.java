package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDetailsDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.entity.VaccineDetails;
import com.project.vaccine.repository.VaccineDetailsRepository;
import com.project.vaccine.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaccineDetailsService {
    @Autowired
    VaccineDetailsRepository vaccineDetailsRepository;

    @Autowired
    VaccineRepository vaccineRepository;

    public List<VaccineDetails> getAllVaccineDetails() {
        return vaccineDetailsRepository.findVaccineDetailsByStatusTrue();
    }

    public VaccineDetails create(VaccineDetailsDTO vaccineDetailsDTO) {
        Vaccine vaccine = vaccineRepository.findById(vaccineDetailsDTO.getVaccineId())
                .orElseThrow(() -> new RuntimeException("Vaccine not found"));
        VaccineDetails vaccineDetails = new VaccineDetails();
        vaccineDetails.setDose_number(vaccineDetailsDTO.getDose_number());
        vaccineDetails.setDate_after(vaccineDetailsDTO.getDate_after());
        vaccineDetails.setStatus(true);
        vaccineDetails.setCreate_At(LocalDateTime.now());
        vaccineDetails.setUpdate_At(LocalDateTime.now());
        vaccineDetails.setPrice(vaccineDetailsDTO.getPrice());
        vaccineDetails.setVaccine(vaccine);
        return vaccineDetailsRepository.save(vaccineDetails);
    }

    public VaccineDetails delete(Long id) {
        VaccineDetails newVacineDetails = vaccineDetailsRepository.findVaccineDetailsById(id);
        newVacineDetails.setStatus(false);
        return vaccineDetailsRepository.save(newVacineDetails);
    }

    public VaccineDetails update(Long id,VaccineDetailsDTO vaccineDetailsDTO) {
        VaccineDetails newVacineDetails = vaccineDetailsRepository.findVaccineDetailsById(id);
        newVacineDetails.setDose_number(vaccineDetailsDTO.getDose_number());
        newVacineDetails.setDate_after(vaccineDetailsDTO.getDate_after());
        newVacineDetails.setUpdate_At(LocalDateTime.now());
        newVacineDetails.setPrice(vaccineDetailsDTO.getPrice());
        newVacineDetails.setVaccine(newVacineDetails.getVaccine());
        return vaccineDetailsRepository.save(newVacineDetails);
    }
}
