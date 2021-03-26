package com.example.demo.model.service;

import com.example.demo.Jwt.UserPrinciple;
import com.example.demo.model.entities.User;
import com.example.demo.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public void saveAll(Set<User> userSet) {
        for (User user:userSet) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.saveAll(userSet);
    }

    @Override
    public User saveWithOutEncodePass(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean remove(int id) {
        if(findById(id).isPresent()){
                userRepository.deleteById(id);
                return true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        return UserPrinciple.build(userOptional.get());
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByAccount_number(int accountNumber) {
        List<User> userList = (List<User>) searchAccountNumber(accountNumber);
        return !userList.isEmpty();
    }

    @Override
    public Iterable<User> searchName(String keyword) {
        keyword = "%"+keyword+"%";
        return userRepository.searchName(keyword);
    }

    @Override
    public Iterable<User> searchAddress(String keyword) {
        keyword = "%"+keyword+"%";
        return userRepository.searchAddress(keyword);
    }

    @Override
    public Iterable<User> searchAccountNumber(int accountNumber) {
        return userRepository.searchAccountNumber(accountNumber);
    }

    @Override
    public Iterable<User> searchBalanceG(int balance) {
        return userRepository.searchBalanceG(balance);
    }

    @Override
    public Iterable<User> searchBalanceL(int balance) {
        return userRepository.searchBalanceL(balance);
    }


}
