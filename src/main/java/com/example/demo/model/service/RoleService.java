package com.example.demo.model.service;

import com.example.demo.model.entities.Role;

import java.util.Optional;

public interface RoleService {
    Iterable<Role> findAll();

    Optional<Role> findById(int id);

    Role save(Role t);

    void remove(int id);
    Optional<Role> findByName(String name);
}
