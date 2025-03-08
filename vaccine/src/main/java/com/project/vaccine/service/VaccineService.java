package com.project.vaccine.service;

import com.project.vaccine.dto.VaccineDTO;
import com.project.vaccine.dto.VaccineDetailsDTO;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VaccineService {

    @Autowired
    private VaccineRepository vaccineRepository;

    @Autowired
    private VaccineDetailsRepository vaccineDetailsRepository;

    @Autowired
    private final ModelMapper modelMapper; // configuration in ModelMapperConfig

    public VaccineService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        modelMapper.typeMap(VaccineDTO.class, Vaccine.class)
                .addMappings(mapper
                        -> mapper.skip(Vaccine::setId));
        modelMapper.typeMap(VaccineDetailsDTO.class, VaccineDetails.class)
                .addMappings(mapper
                        -> mapper.skip(VaccineDetails::setId));
    } // skip id when mapping


    public VaccineResponse createVaccine(VaccineDTO vaccineDTO) {
        LocalDateTime now = LocalDateTime.now();
        /*
         - Nếu vaccine tồn tại và status = false thì cho phép tạo mới bằng cách update lại thông tin cũ
         - Nếu vaccine tồn tại và status = true thì không cho phép tạo mới
        * */

        if (vaccineDTO.getVaccineDetails() == null || vaccineDTO.getVaccineDetails().isEmpty()) {
            throw new InvalidDataException("Vaccine details is required");
        }

        Vaccine existingVaccine = checkExistingVaccine(vaccineDTO.getName());
        if (existingVaccine != null) {
            if (existingVaccine.isStatus()) {
                throw new DuplicateException("Vaccine already exists");
            } else {
                return new VaccineResponse("Vaccine is deleted, update to use vaccine again", existingVaccine);
            }

        }

        Vaccine newVaccine = new Vaccine();
        modelMapper.map(vaccineDTO, newVaccine);
        newVaccine.setCreateAt(now);
        newVaccine.setUpdateAt(now);
        newVaccine.setStatus(true);

        List<VaccineDetails> vaccineDetails = new ArrayList<>();
        Set<Integer> doseNumbers = new HashSet<>();
        for (VaccineDetailsDTO vaccineDetailsDTO : vaccineDTO.getVaccineDetails()) {
            if (doseNumbers.contains(vaccineDetailsDTO.getDoseNumber())) {
                throw new InvalidDataException("Dose number must be unique");
            }// Check dose_number of vaccine details
            doseNumbers.add(vaccineDetailsDTO.getDoseNumber());
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

    public List<VaccineDTO> getAllActiveVaccines() {
        return vaccineRepository.findByStatus(true).stream()
                .map(vaccine -> modelMapper.map(vaccine, VaccineDTO.class))
                .collect(Collectors.toList());
    }


    public List<Vaccine> getAllVaccines() {
        return vaccineRepository.findAll();
    }


    private Vaccine checkExistingVaccine(String name) {
        return vaccineRepository.findByNameIgnoreCase(name)
                .orElse(null);
    }

    public VaccineDTO updateVaccine(Long id, VaccineDTO vaccineDTO) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vaccine not found"));

        if (vaccineRepository.existsByNameAndIdNot(vaccineDTO.getName(), id)) {
            throw new DuplicateException("Vaccine already exists");
        }

        modelMapper.map(vaccineDTO, vaccine);
        vaccine.setUpdateAt(LocalDateTime.now());
        if (!vaccine.isStatus()) {
            vaccine.setStatus(true);
        } // When admin update vaccine, set status = true
        vaccineRepository.save(vaccine);
        return vaccineDTO;
    }

    public VaccineDetailsDTO updateVaccineDetailsByVaccineId(Long vaccineId, Long detailsId, VaccineDetailsDTO vaccineDetailsDTO) {
        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new NotFoundException("Vaccine not found"));



        VaccineDetails vaccineDetails = vaccineDetailsRepository.findByIdAndVaccineId(detailsId, vaccineId)
                .orElseThrow(() -> new NotFoundException("Vaccine details not found"));

        List<VaccineDetails> vaccineDetailsList = vaccineDetailsRepository.findByVaccineId(vaccineId);
        // Size don't need to check because it's handled in createVaccine method
        for (VaccineDetails details : vaccineDetailsList) {
                if (details.getDoseNumber() == vaccineDetailsDTO.getDoseNumber() && !details.getId().equals(detailsId)) {
                    throw new InvalidDataException("Dose number must be unique");
                }// Check dose_number of vaccine details
        }


        // Check does_number of vaccine details
        // Không cho phép cập nhật số lượng vị trí tiêm chủng nếu đã có người đăng ký (nếu đc, hơi khó triển khai)
        // Không cho update giống thứ tư tự tiêm chủng của vaccine đó

        modelMapper.map(vaccineDetailsDTO, vaccineDetails);
        vaccineDetails.setUpdateAt(LocalDateTime.now());
        vaccineDetailsRepository.save(vaccineDetails);
        return vaccineDetailsDTO;
    }

    public void deleteVaccine(Long id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new InvalidDataException("Vaccine not found"));
        vaccine.setStatus(false);
        vaccine.setUpdateAt(LocalDateTime.now());
        vaccineRepository.save(vaccine);
    }

    public void deleteVaccineDetailsByVaccineId(Long vaccineId, Long detailsId) {
        Vaccine vaccine = vaccineRepository.findById(vaccineId)
                .orElseThrow(() -> new NotFoundException("Vaccine not found"));

        VaccineDetails vaccineDetails = vaccineDetailsRepository.findByIdAndVaccineId(detailsId, vaccineId)
                .orElseThrow(() -> new NotFoundException("Vaccine details not found"));

        vaccineDetails.setStatus(false);
        vaccineDetails.setUpdateAt(LocalDateTime.now());
        vaccineDetailsRepository.save(vaccineDetails);
    }
}
