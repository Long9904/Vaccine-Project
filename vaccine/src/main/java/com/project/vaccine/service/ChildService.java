package com.project.vaccine.service;

import com.project.vaccine.config.ModelMapperConfig;
import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.entity.Child;
import com.project.vaccine.exception.DuplicateException;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.ChildRepository;
import com.project.vaccine.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private ModelMapperConfig modelMapperConfig;

    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    public ChildDTO getChildById(Long id) {
        Child child = childRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Child not found with id: " + id));
        return modelMapperConfig.modelMapper().map(child, ChildDTO.class);
    }

    public ChildDTO createChild(ChildDTO childDTO) {
        // Check duplicate child
        if (childRepository.findByName(childDTO.getName()).isPresent()) {
            throw new DuplicateException("Child with the same name already exists");
        }

        // Create child
        LocalDateTime now = LocalDateTime.now();
        Child child = modelMapperConfig.modelMapper().map(childDTO, Child.class);
        child.setStatus(true);
        child.setCreatedAt(now);
        child.setUpdatedAt(now);
        child.setUser(userUtils.getCurrentUser());
        return modelMapperConfig.modelMapper().map(childRepository.save(child), ChildDTO.class);
    }

    public ChildDTO updateChild(Long id, ChildDTO childDTO) {
        Child existingChild = childRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Child not found with id: " + id));

        // Update child details
        modelMapperConfig.modelMapper().map(childDTO, existingChild);
        existingChild.setUpdatedAt(LocalDateTime.now());

        return modelMapperConfig.modelMapper().map(childRepository.save(existingChild), ChildDTO.class);
    }

    public void deleteChild(Long id) {
        Child child = childRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Child not found with id: " + id));
        childRepository.delete(child);
    }
}