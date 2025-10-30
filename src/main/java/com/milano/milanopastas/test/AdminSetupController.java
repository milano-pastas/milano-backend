package com.milano.milanopastas.test;

import com.milano.milanopastas.model.AdminUser;
import com.milano.milanopastas.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/setup")
@RequiredArgsConstructor
public class AdminSetupController {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<AdminUser> createAdmin(@RequestBody AdminUser admin) {
        if (adminUserRepository.findByUsername(admin.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setActive(true);
        AdminUser saved = adminUserRepository.save(admin);
        return ResponseEntity.ok(saved);
    }
}
