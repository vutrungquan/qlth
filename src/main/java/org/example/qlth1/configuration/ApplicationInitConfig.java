package org.example.qlth1.configuration;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.example.qlth1.constant.PredefinedRole;
import org.example.qlth1.entity.Role;
import org.example.qlth1.entity.User;
import org.example.qlth1.repository.RoleRepository;
import org.example.qlth1.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import java.util.HashSet;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(prefix = "spring.datasource", value = "driverClassName", havingValue = "com.mysql.cj.jdbc.Driver")
    public ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application...");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                
                Role adminRole = roleRepository.save(Role.builder()
                            .name(PredefinedRole.ADMIN_ROLE.name())
                            .description("Admin role")
                            .build());
                
                
                    roleRepository.save(Role.builder()
                            .name(PredefinedRole.TEACHER_ROLE.name())
                            .description("Teacher role")
                            .build());
                
                
                    roleRepository.save(Role.builder()
                            .name(PredefinedRole.STUDENT_ROLE.name())
                            .description("Student role")
                            .build());
                
       

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("Admin user has been created with default password: admin, please change it.");
            }
            log.info("Application initialization completed.");
        };
    }
}