package com.project.vaccine.service;

import com.project.vaccine.config.ModelMapperConfig;
import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.entity.Child;
import com.project.vaccine.entity.User;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.ChildRepository;
import com.project.vaccine.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    ModelMapperConfig modelMapperConfig;

    public ChildService(ModelMapperConfig modelMapperConfig) {
        // Skip ID mapping
        modelMapperConfig.modelMapper().typeMap(ChildDTO.class, Child.class)
                .addMappings(mapper -> mapper.skip(Child::setId));
    }

    public ChildDTO createChild( ChildDTO childDTO) {
        LocalDateTime now = LocalDateTime.now();
        Child child = modelMapperConfig.modelMapper().map(childDTO, Child.class);
        child.setStatus(true);
        child.setCreatedAt(now);
        child.setUpdatedAt(now);
        child.setUser(userUtils.getCurrentUser());
        return modelMapperConfig.modelMapper().map(childRepository.save(child), ChildDTO.class);
    }


    public ChildDTO updateChild(Long id, ChildDTO childDTO) {
        LocalDateTime now = LocalDateTime.now();
        User user = userUtils.getCurrentUser();

        // Find the child by user ID and child ID
        Child child = childRepository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new NotFoundException("Child not found or you don't have permission to update this child"));

        // Map child DTO to child entity
        modelMapperConfig.modelMapper().map(childDTO, child);
        child.setUpdatedAt(now);

        // Save updated child
        Child updatedChild = childRepository.save(child);

        // Convert updated child entity back to DTO
        return modelMapperConfig.modelMapper().map(updatedChild, ChildDTO.class);
    }

    public List<ChildDTO> getChildren() {
        User user = userUtils.getCurrentUser();
        List<Child> children = childRepository.findByUserIdAndStatus(user.getId(), true);
        return children.stream().map(child -> modelMapperConfig.modelMapper().
                map(child, ChildDTO.class)).collect(Collectors.toList());
    }

    public void deleteChild(Long id) {
        User user = userUtils.getCurrentUser();
        Child child = childRepository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new NotFoundException("Child not found or you don't have permission to delete this child"));
        // set status to false
        child.setStatus(false);
        childRepository.save(child);
    }

    // Add method to get child by ID, Admin can view all children
    public ChildDTO getChildById(Long id) {
        return null;
    }

    // Add method to get all children, Admin can view all children
    public List<ChildDTO> getAllChildren() {
        return null;
    }

}
