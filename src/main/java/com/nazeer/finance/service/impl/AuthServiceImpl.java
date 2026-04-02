package com.nazeer.finance.service.impl;

import com.nazeer.finance.dto.AuthResponse;
import com.nazeer.finance.dto.LoginRequest;
import com.nazeer.finance.dto.RegisterRequest;
import com.nazeer.finance.entity.AppUser;
import com.nazeer.finance.entity.Role;
import com.nazeer.finance.entity.RoleName;
import com.nazeer.finance.exception.BadRequestException;
import com.nazeer.finance.repository.RoleRepository;
import com.nazeer.finance.repository.UserRepository;
import com.nazeer.finance.security.JwtService;
import com.nazeer.finance.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role viewerRole = roleRepository.findByName(RoleName.VIEWER)
                .orElseThrow(() -> new BadRequestException("Default role not configured"));

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.getRoles().add(viewerRole);

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(),
                user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet()));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));

        Set<String> roles = user.getRoles().stream().map(r -> r.getName().name()).collect(Collectors.toSet());
        return new AuthResponse(token, user.getUsername(), roles);
    }
}
