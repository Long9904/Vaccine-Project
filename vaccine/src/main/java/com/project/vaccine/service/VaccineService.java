package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.dto.VaccineDetailsDTO;
import com.project.vaccine.dto.request.VaccineUpdateRequest;
import com.project.vaccine.dto.response.VaccineResponse;
import com.project.vaccine.entity.Vaccine;
import com.project.vaccine.entity.VaccineDetails;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.InvalidDataException;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.VaccineDetailsRepository;
import com.project.vaccine.repository.VaccineRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineDetailsRepository vaccineDetailsRepository;

    @Autowired
    private final ModelMapper modelMapper; // đc cấu hình từ ModelMapperConfig

    public VaccineService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.typeMap(VaccineDTO.class, Vaccine.class)
                .addMappings(mapper
                        -> mapper.skip(Vaccine::setId));
        modelMapper.typeMap(VaccineDetailsDTO.class, VaccineDetails.class)
                .addMappings(mapper
                        -> mapper.skip(VaccineDetails::setId));
    }


    public VaccineResponse createVaccine(VaccineDTO vaccineDTO) {
        LocalDateTime now = LocalDateTime.now();
        /*
         - Nếu vaccine tồn tại và status = false thì cho phép tạo mới bằng cách update lại thông tin cũ
           + Cập nhật status = true
         - Nếu vaccine tồn tại và status = true thì không cho phép tạo mới
        * */

        if (vaccineDTO.getVaccineDetails() == null || vaccineDTO.getVaccineDetails().isEmpty()) {
            throw new InvalidDataException("Vaccine details is required");
        }

        Vaccine existingVaccine = checkExistingVaccine(vaccineDTO.getName());
        if (existingVaccine != null) {
            if (existingVaccine.isStatus()) {
                throw new DuplicateException("Vaccine already exists");
            }
            existingVaccine.setStatus(true);
            existingVaccine.setUpdateAt(now);
            vaccineRepository.save(existingVaccine);
            return new VaccineResponse("Vaccine has been overridden", existingVaccine);
        }

        Vaccine newVaccine = new Vaccine();
        modelMapper.map(vaccineDTO, newVaccine);
        newVaccine.setCreateAt(now);
        newVaccine.setUpdateAt(now);
        newVaccine.setStatus(true);

        List<VaccineDetails> vaccineDetails = new ArrayList<>();

        for (VaccineDetailsDTO vaccineDetailsDTO : vaccineDTO.getVaccineDetails()) {
            VaccineDetails details = new VaccineDetails();
            modelMapper.map(vaccineDetailsDTO, details);
            details.setCreateAt(now);
            details.setUpdateAt(null);
            details.setStatus(true);
            details.setVaccine(newVaccine);
            vaccineDetails.add(details);
        } // Create vaccine details

        newVaccine.setVaccineDetails(vaccineDetails);
        vaccineRepository.save(newVaccine);
        return new VaccineResponse("Vaccine has been created", newVaccine);
    }

    public List<Vaccine> getAllActiveVaccines() {
        return vaccineRepository.findByStatus(true);
    }


    public List<Vaccine> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    public void deleteVaccine(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Vaccine not found"));
        vaccine.setStatus(false);
        vaccine.setUpdateAt(LocalDateTime.now());
        vaccineRepository.save(vaccine);
    }

    private Vaccine checkExistingVaccine(String name) {
        return vaccineRepository.findByNameIgnoreCase(name)
                .orElse(null);
    }

    public VaccineUpdateRequest updateVaccine(Long id, VaccineUpdateRequest request) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vaccine not found"));

        if (vaccineRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new DuplicateException("Vaccine already exists");
        }
        modelMapper.map(request, vaccine);
        vaccine.setUpdateAt(LocalDateTime.now());
        vaccineRepository.save(vaccine);
        return request;
    }
}
