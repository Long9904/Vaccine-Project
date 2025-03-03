package com.project.vaccine.service;

import com.project.vaccine.config.ModelMapperConfig;
import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.entity.Child;
import com.project.vaccine.repository.ChildRepository;
import com.project.vaccine.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    ModelMapperConfig modelMapperConfig;


    public ChildDTO createChild( ChildDTO childDTO) {
        // Check duplicate child

        // Create child
        LocalDateTime now = LocalDateTime.now();
        Child child = modelMapperConfig.modelMapper().map(childDTO, Child.class);
        child.setStatus(true);
        child.setCreatedAt(now);
        child.setUpdatedAt(now);
        child.setUser(userUtils.getCurrentUser());
        return modelMapperConfig.modelMapper().map(childRepository.save(child), ChildDTO.class);
    }
}
