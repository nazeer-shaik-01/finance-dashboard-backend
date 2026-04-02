package com.nazeer.finance.config;

import com.nazeer.finance.entity.Role;
import com.nazeer.finance.entity.RoleName;
import com.nazeer.finance.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> Arrays.stream(RoleName.values())
                .forEach(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(new Role(roleName))));
    }
}
