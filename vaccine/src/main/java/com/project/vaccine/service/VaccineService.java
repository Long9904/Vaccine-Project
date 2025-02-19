package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.dto.VaccineDetailsDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.entity.VaccineDetails;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.ErrorDetail;
import com.project.vaccine.repository.VaccineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;


    public Vaccine createVaccine(VaccineDTO vaccineDTO) {

        List<ErrorDetail> errors = new ArrayList<>();

        if(vaccineRepository.findByName(vaccineDTO.getName()).isPresent()) {
            errors.add(new ErrorDetail("name", "Name already exists"));
        }

        if(!errors.isEmpty()) {
            throw new DuplicateException(errors);
        }

        Vaccine vaccine = new Vaccine();
        vaccine.setName(vaccineDTO.getName());
        vaccine.setDescription(vaccineDTO.getDescription());
        vaccine.setQuantity(vaccineDTO.getQuantity());
        vaccine.setCreate_At(LocalDateTime.now());
        vaccine.setUpdate_At(LocalDateTime.now());
        vaccine.setStatus(true);

        List<VaccineDetails> vaccineDetails = new ArrayList<>();

        if(vaccineDTO.getVaccineDetails() != null) {
            for(VaccineDetailsDTO vaccineDetailsDTO : vaccineDTO.getVaccineDetails()) {
                VaccineDetails details = new VaccineDetails();
                details.setDose_number(vaccineDetailsDTO.getDose_number());
                details.setInterval_days(vaccineDetailsDTO.getInterval_days());
                details.setPrice(vaccineDetailsDTO.getPrice());
                details.setSide_effect(vaccineDetailsDTO.getSide_effect());
                details.setRecommended(vaccineDetailsDTO.isRecommended());
                details.setCreate_At(LocalDateTime.now());
                details.setStatus(true);
                details.setVaccine(vaccine);
                vaccineDetails.add(details);
            }
        }
        vaccine.setVaccineDetails(vaccineDetails);
        return vaccineRepository.save(vaccine);
    }

    public List<Vaccine> getAllVaccines() {
        return vaccineRepository.findAll();
    }
}
