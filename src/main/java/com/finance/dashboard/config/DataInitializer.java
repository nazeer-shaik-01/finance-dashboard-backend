package com.finance.dashboard.config;

import com.finance.dashboard.entity.Category;
import com.finance.dashboard.entity.Role;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.CategoryRepository;
import com.finance.dashboard.repository.RoleRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Seed roles
            if (roleRepository.count() == 0) {
                roleRepository.save(Role.builder().name(Constants.ROLE_ADMIN).description("Full system access").build());
                roleRepository.save(Role.builder().name(Constants.ROLE_ANALYST).description("Read and manage records").build());
                roleRepository.save(Role.builder().name(Constants.ROLE_VIEWER).description("Read-only access").build());
                log.info("Roles seeded");
            }

            // Seed categories
            if (categoryRepository.count() == 0) {
                categoryRepository.save(Category.builder().name("Salary").type(Category.CategoryType.INCOME).description("Monthly salary").icon("💰").build());
                categoryRepository.save(Category.builder().name("Bonus").type(Category.CategoryType.INCOME).description("Performance bonus").icon("🎁").build());
                categoryRepository.save(Category.builder().name("Freelance").type(Category.CategoryType.INCOME).description("Freelance income").icon("💻").build());
                categoryRepository.save(Category.builder().name("Investment").type(Category.CategoryType.INCOME).description("Investment returns").icon("📈").build());
                categoryRepository.save(Category.builder().name("Food").type(Category.CategoryType.EXPENSE).description("Food and dining").icon("🍔").build());
                categoryRepository.save(Category.builder().name("Transport").type(Category.CategoryType.EXPENSE).description("Transportation").icon("🚗").build());
                categoryRepository.save(Category.builder().name("Shopping").type(Category.CategoryType.EXPENSE).description("Shopping").icon("🛍️").build());
                categoryRepository.save(Category.builder().name("Entertainment").type(Category.CategoryType.EXPENSE).description("Entertainment").icon("🎬").build());
                categoryRepository.save(Category.builder().name("Utilities").type(Category.CategoryType.EXPENSE).description("Utilities and bills").icon("⚡").build());
                categoryRepository.save(Category.builder().name("Healthcare").type(Category.CategoryType.EXPENSE).description("Medical expenses").icon("🏥").build());
                log.info("Categories seeded");
            }

            // Seed admin user
            if (!userRepository.existsByEmail("admin@finance.com")) {
                Role adminRole = roleRepository.findByName(Constants.ROLE_ADMIN).orElseThrow();
                userRepository.save(User.builder()
                        .email("admin@finance.com")
                        .firstName("Admin")
                        .lastName("User")
                        .password(passwordEncoder.encode("admin123"))
                        .role(adminRole)
                        .status(User.UserStatus.ACTIVE)
                        .build());
                log.info("Admin user seeded: admin@finance.com / admin123");
            }
        };
    }
}
