package com.devtrack.service;

import com.devtrack.exception.ResourceNotFoundException;
import com.devtrack.model.User;
import com.devtrack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User userDetails) {
        User user = getUserById(userId);
        if (userDetails.getEmail() != null)
            user.setEmail(userDetails.getEmail());
        if (userDetails.getFullName() != null)
            user.setFullName(userDetails.getFullName());
        if (userDetails.getProfilePic() != null)
            user.setProfilePic(userDetails.getProfilePic());
        return userRepository.save(user);
    }

    public void updateStreak(Long userId, boolean codedToday) {
        User user = getUserById(userId);
        if (codedToday) {
            user.incrementStreak();
        } else {
            user.resetStreak();
        }
        userRepository.save(user);
    }

    public void addHours(Long userId, Double hours) {
        User user = getUserById(userId);
        user.addHours(hours);
        userRepository.save(user);
    }

    public void addProblemsSolved(Long userId, Integer count) {
        User user = getUserById(userId);
        user.addProblemsSolved(count);
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}
