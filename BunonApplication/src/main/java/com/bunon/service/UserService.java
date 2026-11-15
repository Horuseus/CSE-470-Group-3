package com.bunon.service;

import com.bunon.model.Role;
import com.bunon.model.User;
import com.bunon.model.Weaver;
import com.bunon.repository.UserRepository;
import com.bunon.repository.WeaverRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Sign-up and account rules. Problems are reported with IllegalArgumentException(message); controllers show the message. */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final WeaverRepository weaverRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, WeaverRepository weaverRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.weaverRepository = weaverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(Role role, String name, String username, String email, String phone, String password,
                         String district, String specialization, String deliveryAddress) {
        // Administrators are created by staff, never through the public form.
        if (role == Role.ADMIN) {
            throw new IllegalArgumentException("Please choose an account type.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please enter your name.");
        }
        String cleanUsername = cleanUsername(username);
        if (userRepository.existsByUsername(cleanUsername)) {
            throw new IllegalArgumentException("That username is already taken. Please pick another.");
        }
        String cleanEmail = email == null ? "" : email.trim().toLowerCase();
        if (!cleanEmail.contains("@") || cleanEmail.length() < 5) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new IllegalArgumentException("An account with this email already exists. Try logging in.");
        }
        checkPassword(password);
        boolean isWeaverType = role == Role.WEAVER || role == Role.COOPERATIVE_MEMBER;
        if (isWeaverType && (district == null || district.isBlank())) {
            throw new IllegalArgumentException("Weavers must enter their district.");
        }

        User user = new User(name.trim(), cleanEmail, phone, passwordEncoder.encode(password), role);
        user.setUsername(cleanUsername);
        if (role == Role.CUSTOMER) {
            user.setDeliveryAddress(deliveryAddress);
        }
        user = userRepository.save(user);

        if (isWeaverType) {      // weavers and cooperative members also get a weaver profile
            weaverRepository.save(new Weaver(user.getName(), district.trim(),
                    specialization == null ? "" : specialization.trim(), "", user));
        }
        return user;
    }

    /** Save the "My account" form. newPhoto is a file name, or null to keep the old picture. */
    public void updateProfile(String email, String name, String username, String phone, String district,
                              String shopAddress, String bio, String deliveryAddress, String newPhoto) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Please enter your name.");
        }
        String cleanUsername = cleanUsername(username);
        userRepository.findByUsername(cleanUsername).ifPresent(other -> {
            if (!other.getId().equals(user.getId())) {
                throw new IllegalArgumentException("That username is already taken. Please pick another.");
            }
        });

        user.setName(name.trim());
        user.setUsername(cleanUsername);
        user.setPhone(phone);
        if (newPhoto != null) {
            user.setProfileImage(newPhoto);
        }
        if (user.getRole() == Role.CUSTOMER) {
            user.setDeliveryAddress(deliveryAddress);
        }
        userRepository.save(user);

        // Weavers also have a weaver profile: this is where "shop location" lives.
        weaverRepository.findByUserEmail(email).ifPresent(weaver -> {
            weaver.setName(user.getName());
            if (district != null && !district.isBlank()) {
                weaver.setDistrict(district.trim());
            }
            weaver.setShopAddress(shopAddress);
            weaver.setBio(bio);
            weaverRepository.save(weaver);
        });
    }

    public void changePassword(String email, String currentPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Your current password is not correct.");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("The two new passwords do not match.");
        }
        checkPassword(newPassword);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void checkPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Your password needs at least 8 characters.");
        }
    }

    private String cleanUsername(String username) {
        String clean = username == null ? "" : username.trim().toLowerCase();
        if (!clean.matches("[a-z0-9_]{3,20}")) {
            throw new IllegalArgumentException("Username must be 3 to 20 characters: small letters, numbers or underscore.");
        }
        return clean;
    }
}
