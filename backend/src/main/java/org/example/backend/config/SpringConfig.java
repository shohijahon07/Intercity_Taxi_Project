package org.example.backend.config;


import lombok.RequiredArgsConstructor;
import org.example.backend.entity.User;
import org.example.backend.repository.UserRepo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SpringConfig {
    private final MyFilter myFilter;
    private final UserRepo userRepo;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable).csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/**","/login","/index.html","/assets/**","/static/**","/*ico","/*.json","/*.png").permitAll()
                        .requestMatchers("/api/auth/login","/api/advertising","/api/auth/name","/api/auth/refresh","/api/driver","/api/driver/bydriver","/api/user","/api/auth/check","/api/toCity","/api/fromCity","/api/user/drivers","/api/user/isDrive","/api/driver/bydel").permitAll()
                        .requestMatchers("/api/fileController","/api/fileController/photo","/api/user/countD","/api/user/countU","/api/user/search","/api/driver/byDate","/api/driver/byDay","/api/driver/byTime").permitAll()
                        .requestMatchers("/api/auth/login","/api/user/save/**","/api/auth/name","/api/driver","/api/driver/bydriver","/api/user","/api/toCity","/api/fromCity","/api/user/drivers","/api/user/isDrive").permitAll()
                        .requestMatchers("/api/fileController","/api/fileController/photo","/api/user/save","/api/user/countD","/api/user/countU","/api/comment","/api/user/pessenger").permitAll()
                        .requestMatchers("/","/index.html","/static/**","/*.ico",
                                "/*.json","/*.png","/*.svg","/*.mp3","/*.mp4","/*.jpeg","/*.m4a","/*.M4A","/*.webm","/*.WEBP","/*.webp",
                                "/*.gif","/*.wav","/*.ogg","/*.jpg","/*.pdf"
                                ).permitAll()
                        .anyRequest().authenticated()
        ).addFilterBefore(myFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            User user = userRepo.findByPhoneNumber(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not found "));
            return new org.springframework.security.core.userdetails.User(
                    user.getPhoneNumber(),
                    user.getPassword(),
                    new ArrayList<>()
             );

        };
    }



    @Bean
    public BCryptPasswordEncoder encoder(){return new BCryptPasswordEncoder();}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}