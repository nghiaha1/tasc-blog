package com.tasc.blogging.seeder;

import com.tasc.blogging.entity.enums.BaseStatus;
import com.tasc.blogging.entity.user.Role;
import com.tasc.blogging.entity.user.User;
import com.tasc.blogging.repository.RoleRepository;
import com.tasc.blogging.repository.UserRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static int ITEM_COUNT = 100;

    private Faker faker = new Faker();

    public void seed() {
        Role role = roleRepository.findByName("DEFAULT");
        if (role == null) {
            roleRepository.save(Role.builder()
                    .name("DEFAULT")
                    .build());
        }

        for (int i = 0; i < ITEM_COUNT; i++) {
            userRepository.save(User.builder()
                    .firstName(faker.name().firstName())
                    .lastName(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .password(passwordEncoder.encode(faker.internet().password()))
                    .bio(faker.lorem().paragraph())
                    .role(role)
                    .status(BaseStatus.ACTIVE)
                    .build());
        }
    }
}
