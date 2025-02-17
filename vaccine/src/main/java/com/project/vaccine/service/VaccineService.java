package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VaccineService {
    @Autowired
    private VaccineRepository vaccineRepository;

    public List<Vaccine> getAllVaccine() {
        return vaccineRepository.findVaccinesByStatusTrue();
    }

    public Vaccine create(VaccineDTO vaccineDTO) {
        Vaccine vaccine = new Vaccine();
        vaccine.setName(vaccineDTO.getName());
        vaccine.setDescription(vaccineDTO.getDescription());
        vaccine.setStatus(true);
        vaccine.setQuantity(vaccineDTO.getQuantity());
        vaccine.setCreate_At(LocalDateTime.now());
        vaccine.setUpdate_At(LocalDateTime.now());
//        vaccine.setMin_age(vaccineDTO.getMin_age());
//        vaccine.setMax_age(vaccineDTO.getMax_age());
        return vaccineRepository.save(vaccine);
    }

    public Vaccine delete(Long id) {
        Vaccine vaccine = vaccineRepository.findVaccineById(id);
        vaccine.setStatus(false);
        return vaccineRepository.save(vaccine);
    }

    public Vaccine update(Long id, VaccineDTO vaccine) {
        Vaccine newVaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccine not found with id " + id));

        newVaccine.setName(vaccine.getName());
        newVaccine.setDescription(vaccine.getDescription());
        newVaccine.setQuantity(vaccine.getQuantity());
        newVaccine.setUpdate_At(LocalDateTime.now());
//        newVaccine.setMin_age(vaccine.getMin_age());
//        newVaccine.setMax_age(vaccine.getMax_age());

        return vaccineRepository.save(newVaccine);
    }
}
