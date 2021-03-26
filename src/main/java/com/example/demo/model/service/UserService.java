package com.example.demo.model.service;

import com.example.demo.model.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;
import java.util.Set;

public interface UserService extends UserDetailsService {
    Iterable<User> findAll();

    Optional<User> findById(int id);

    User save(User t);

    void saveAll(Set<User> userSet);

    boolean remove(int id);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByAccount_number(int accountNumber);

    User saveWithOutEncodePass(User user);

    Iterable<User> searchName(String keyword);

    Iterable<User> searchAddress(String keyword);

    Iterable<User> searchAccountNumber(int accountNumber);

    Iterable<User> searchBalanceG(int balance);

    Iterable<User> searchBalanceL(int balance);
}
