package com.demo.configuration;

import com.demo.entity.User;
import com.demo.enums.Roles;
import com.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByName("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Roles.ADMIN.name());

                User user = User.builder()
                        .name("admin")
                        .password(passwordEncoder.encode("admin"))
                        //.roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user has been creater with deafalt password! Please changes it");
            }
        };
    }
}
