package com.project.vaccine.service;

import com.project.vaccine.dto.request.UserRequest;
import com.project.vaccine.entity.User;
import com.project.vaccine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public boolean updateUser(long id, UserRequest userRequest){

        User user = userRepository.findUserById(id).orElse(null);
        if(user != null){
            return true;
        }
        return false;
    }
}
