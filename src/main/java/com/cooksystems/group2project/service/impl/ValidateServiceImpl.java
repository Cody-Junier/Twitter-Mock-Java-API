package com.cooksystems.group2project.service.impl;


import com.cooksystems.group2project.repositories.HashtagRepository;
import com.cooksystems.group2project.repositories.UserRepository;
import com.cooksystems.group2project.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
    @RequiredArgsConstructor
    public class ValidateServiceImpl implements ValidateService {

        private final UserRepository userRepository;
        private final HashtagRepository hashtagRepository;


        @Override
        public boolean validateHashtagExists(String label) {
            return hashtagRepository.findByLabelIgnoreCase(label) != null;
        }


}
