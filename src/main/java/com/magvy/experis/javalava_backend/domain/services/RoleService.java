package com.magvy.experis.javalava_backend.domain.services;

import com.magvy.experis.javalava_backend.infrastructure.repositories.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
