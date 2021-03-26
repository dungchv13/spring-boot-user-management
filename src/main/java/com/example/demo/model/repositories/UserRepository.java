package com.example.demo.model.repositories;

import com.example.demo.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
//    boolean existsByAccount_number(int accountNumber);


    @Query(value = "select * from user where " +
            "email like ?1 \n" +
            "or firstname like ?1 \n" +
            "or lastname like ?1 ;", nativeQuery = true)
    Iterable<User> searchName(String keyword);

    @Query(value = "select * from user where \n" +
            "address like ?1 \n" +
            "or city like ?1 \n" +
            "or state like ?1 ;", nativeQuery = true)
    Iterable<User> searchAddress(String keyword);

    @Query(value = "select * from user where account_number = ?1", nativeQuery = true)
    Iterable<User> searchAccountNumber(int accountNumber);

    @Query(value = "select * from user where balance >= ?1 ", nativeQuery = true)
    Iterable<User> searchBalanceG(int balance);

    @Query(value = "select * from user where balance <= ?1 ", nativeQuery = true)
    Iterable<User> searchBalanceL(int balance);
}
