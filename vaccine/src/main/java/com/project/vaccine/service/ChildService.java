package com.project.vaccine.service;

import com.project.vaccine.dto.ChildDTO;
import com.project.vaccine.entity.Child;
import com.project.vaccine.entity.User;
import com.project.vaccine.exception.NotFoundException;
import com.project.vaccine.repository.ChildRepository;
import com.project.vaccine.utils.UserUtils;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    public ChildService(ModelMapper modelMapper) {
        // Skip ID mapping
        this.modelMapper = modelMapper;
        this.modelMapper.typeMap(ChildDTO.class, Child.class)
                .addMappings(mapper -> mapper.skip(Child::setId));
    }

    public ChildDTO createChildByCurrentUser(ChildDTO childDTO) {
        LocalDateTime now = LocalDateTime.now();
        Child child = modelMapper.map(childDTO, Child.class);
        child.setStatus(true);
        child.setCreatedAt(now);
        child.setUpdatedAt(now);
        child.setUser(userUtils.getCurrentUser());
        return modelMapper.map(childRepository.save(child), ChildDTO.class);
    }


    public ChildDTO updateChildByCurrentUser(Long id, ChildDTO childDTO) {
        LocalDateTime now = LocalDateTime.now();
        User user = userUtils.getCurrentUser();

        // Find the child by user ID and child ID
        Child child = childRepository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new NotFoundException("Child not found or you don't have permission to update this child"));

        // Map child DTO to child entity
        modelMapper.map(childDTO, child);
        child.setUpdatedAt(now);

        // Save updated child
        Child updatedChild = childRepository.save(child);

        // Convert updated child entity back to DTO
        return modelMapper.map(updatedChild, ChildDTO.class);
    }

    public List<ChildDTO> getChildrenByCurrentUser() {
        User user = userUtils.getCurrentUser();
        List<Child> children = childRepository.findByUserIdAndStatus(user.getId(), true);
        return children.stream().map(child -> modelMapper.
                map(child, ChildDTO.class)).collect(Collectors.toList());
    }

    public void deleteChildByCurrentUser(Long id) {
        User user = userUtils.getCurrentUser();
        Child child = childRepository.findByIdAndUserId(id, user.getId()).orElseThrow(()
                -> new NotFoundException("Child not found"));
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
