package com.grocerycompare.backend.service;

import com.grocerycompare.backend.DTO.AuthRequest;
import com.grocerycompare.backend.DTO.AuthResponse;
import com.grocerycompare.backend.DTO.RegisterRequest;
import com.grocerycompare.backend.DTO.UserDTO;
import com.grocerycompare.backend.entity.Role;
import com.grocerycompare.backend.entity.User;
import com.grocerycompare.backend.exception.DuplicateEmailException;
import com.grocerycompare.backend.exception.InvalidCredentialsException;
import com.grocerycompare.backend.exception.UserNotFoundException;
import com.grocerycompare.backend.repository.UserRepository;
import com.grocerycompare.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        Role userRole = request.getRole() != null ? request.getRole() : Role.ROLE_USER;
        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                userRole
        );

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole().name());

        return new AuthResponse(token, savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public UserDTO getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateProfile(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }
        return new UserDTO(userRepository.save(user));
    }
}
