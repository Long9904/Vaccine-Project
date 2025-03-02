package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.dto.VaccineDetailsDTO;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.entity.VaccineDetails;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.InvalidDataException;
import com.project.vaccine.repository.VaccineDetailsRepository;
import com.project.vaccine.repository.VaccineRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineDetailsRepository vaccineDetailsRepository;

    @Autowired
    private ModelMapper modelMapper;

    //nếu data input xóa lun vacineDetails thì không báo lỗi, còn nếu để vaccineDetails mà list đối tượng là rỗng thì báo lỗi
    public Vaccine createVaccine(VaccineDTO vaccineDTO) {

        if(vaccineRepository.findByNameIgnoreCase(vaccineDTO.getName().trim()).isPresent()) {
            throw new DuplicateException("Vaccine name already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        Vaccine vaccine = new Vaccine();
        modelMapper.map(vaccineDTO, vaccine);
        vaccine.setCreateAt(now);
        vaccine.setUpdateAt(now);
        vaccine.setStatus(true);

        List<VaccineDetails> vaccineDetails = new ArrayList<>();

        if(vaccineDTO.getVaccineDetails() != null) {
            for(VaccineDetailsDTO vaccineDetailsDTO : vaccineDTO.getVaccineDetails()) {
                VaccineDetails details = new VaccineDetails();
                modelMapper.map(vaccineDetailsDTO, details);
                details.setCreateAt(now);
                details.setUpdateAt(now);
                details.setStatus(true);
                details.setVaccine(vaccine);
                vaccineDetails.add(details);
            }
           
        }
        vaccine.setVaccineDetails(vaccineDetails);
        return vaccineRepository.save(vaccine);
    }

    public List<Vaccine> getVaccinesByStatus() {
        return vaccineRepository.findByStatus(true);
    }

    public List<VaccineDetails> getVaccineDetails() {
        return vaccineDetailsRepository.findAll();
    }
}
