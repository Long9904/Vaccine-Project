package com.project.vaccine.service;

import com.project.vaccine.entity.Child;
import com.project.vaccine.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    public Child createChild(Child child) {
        return childRepository.save(child);
    }

    public Child getChildById(Long id) {
        Optional<Child> child = childRepository.findById(id);
        return child.orElse(null);
    }

    public List<Child> getAllChildren() {
        return childRepository.findAll();
    }

    public Child updateChild(Long id, Child child) {
        Optional<Child> existingChild = childRepository.findById(id);
        if (existingChild.isPresent()) {
            Child updatedChild = existingChild.get();
            updatedChild.setName(child.getName());
            updatedChild.setDateOfBirth(child.getDateOfBirth());
            updatedChild.setParentName(child.getParentName());
            return childRepository.save(updatedChild);
        } else {
            return null;
        }
    }

    public void deleteChild(Long id) {
        childRepository.deleteById(id);
    }
}