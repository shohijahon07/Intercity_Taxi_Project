package org.example.backend.config;


import org.example.backend.entity.Role;
import org.example.backend.entity.User;
import org.example.backend.repository.RoleRepo;
import org.example.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

@Configuration
public class Loader implements CommandLineRunner {

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {

        List<Role> all = roleRepo.findAll();
        if (all.isEmpty()){
            List<Role> roles = roleRepo.saveAll(List.of(


                    new Role("ROLE_ADMIN")

            ));
            User user = new User("Shohijahon","+99800000000",passwordEncoder.encode("0000000000"),null,null,null,null,null,false,null,roles);
            userRepo.save(user);
        }
    }
}
