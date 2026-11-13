package com.bunon.service;

import com.bunon.dto.CustomerRegistrationForm;
import com.bunon.dto.WeaverRegistrationForm;
import com.bunon.model.*;
import com.bunon.repository.UserRepository;
import com.bunon.repository.VerificationApplicationRepository;
import com.bunon.repository.WeaverRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** F16 - registration part. Working baseline; the F16 owner improves validation & edge cases. */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final WeaverRepository weaverRepository;
    private final VerificationApplicationRepository applicationRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorage;

    public AuthService(UserRepository userRepository, WeaverRepository weaverRepository,
                       VerificationApplicationRepository applicationRepository,
                       PasswordEncoder passwordEncoder, FileStorageService fileStorage) {
        this.userRepository = userRepository;
        this.weaverRepository = weaverRepository;
        this.applicationRepository = applicationRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorage = fileStorage;
    }

    @Transactional
    public void registerWeaver(WeaverRegistrationForm form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("This email is already registered");
        }
        if (weaverRepository.existsByNidNumber(form.getNidNumber())) {
            throw new IllegalArgumentException("This NID is already registered");
        }

        User user = new User();
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setPhone(form.getPhone());
        user.setRole(Role.WEAVER);
        userRepository.save(user);

        Weaver weaver = new Weaver();
        weaver.setUser(user);
        weaver.setLocation(form.getLocation());
        weaver.setSpecialization(form.getSpecialization());
        weaver.setNidNumber(form.getNidNumber());
        weaver.setNidImagePath(fileStorage.store(form.getNidImage(), "nid"));
        weaverRepository.save(weaver);

        VerificationApplication application = new VerificationApplication();
        application.setWeaver(weaver);
        applicationRepository.save(application); // status = PENDING
    }

    @Transactional
    public void registerCustomer(CustomerRegistrationForm form) {
        if (userRepository.existsByEmail(form.getEmail())) {
            throw new IllegalArgumentException("This email is already registered");
        }
        User user = new User();
        user.setFullName(form.getFullName());
        user.setEmail(form.getEmail());
        user.setPassword(passwordEncoder.encode(form.getPassword()));
        user.setPhone(form.getPhone());
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
    }
}
