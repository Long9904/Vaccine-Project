package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        vaccine.setStatus(vaccineDTO.isStatus());
        vaccine.setQuantity(vaccineDTO.getQuantity());
        vaccine.setCreate_At(vaccineDTO.getCreate_At());
        vaccine.setUpdate_At(vaccineDTO.getUpdate_At());
        vaccine.setMin_age(vaccineDTO.getMin_age());
        vaccine.setMax_age(vaccineDTO.getMax_age());
        return vaccineRepository.save(vaccine);
    }
}
