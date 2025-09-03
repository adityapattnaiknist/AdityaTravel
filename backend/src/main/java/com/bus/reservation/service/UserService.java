package com.bus.reservation.service;

import com.bus.reservation.entity.User;
import com.bus.reservation.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) { return userRepository.save(user); }
    public Optional<User> findById(Long id) { return userRepository.findById(id); }
    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
    public List<User> findAll() { return userRepository.findAll(); }
    public void deleteById(Long id) { userRepository.deleteById(id); }
    public User update(Long id, User updated) { updated.setId(id); return userRepository.save(updated); }
}

