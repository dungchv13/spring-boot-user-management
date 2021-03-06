package com.example.demo.controller;

import com.example.demo.model.entities.Role;
import com.example.demo.model.entities.User;
import com.example.demo.model.service.RoleService;
import com.example.demo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> getUsers() {
        List<User> userList = (List<User>) userService.findAll();
        return userList;
    }

    @GetMapping("/search/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public User getUser(@PathVariable("id") int id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return userService.findById(id).get();
        }
        return null;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setUsername(user.getEmail());
        if (userService.existsByUsername(user.getUsername()) ||
                userService.existsByEmail(user.getEmail()) ||
                userService.existsByAccount_number(user.getAccount_number())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleService.findById(2).get();
        roles.add(userRole);
        user.setRoles(roles);

        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        User user1 = userService.save(user);
        return new ResponseEntity(user1, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable("id") int id) {
        boolean isDeleted = userService.remove(id);
        if (isDeleted) {
            return new ResponseEntity<>("User has been removed", HttpStatus.OK);
        }
        return new ResponseEntity<>("Cant find user", HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        Optional<User> oldUser = userService.findById(id);
        if (oldUser.isPresent()) {

            if (!user.getUsername().equals(oldUser.get().getUsername())) {
                if (userService.existsByUsername(user.getUsername())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                oldUser.get().setUsername(user.getUsername());
            }

            if (!user.getEmail().equals(oldUser.get().getEmail())) {
                if (userService.existsByEmail(user.getEmail())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                oldUser.get().setEmail(user.getEmail());
            }

            if (user.getAccount_number() != oldUser.get().getAccount_number()) {
                if (userService.existsByAccount_number(user.getAccount_number())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                oldUser.get().setAccount_number(user.getAccount_number());
            }

            oldUser.get().setBalance(user.getBalance());

            oldUser.get().setFirstname(user.getFirstname());
            oldUser.get().setLastname(user.getLastname());

            oldUser.get().setAge(user.getAge());

            oldUser.get().setGender(user.getGender());

            oldUser.get().setCity(user.getCity());

            oldUser.get().setState(user.getState());

            oldUser.get().setEmail(user.getEmail());

            oldUser.get().setAddress(user.getAddress());

            Set<Role> roles = new HashSet<>();
            user.getRoles().forEach(role -> {
                if (role.getName().equals("admin") || role.getName().equals("ROLE_ADMIN")) {
                    Role adminRole = roleService.findByName("ROLE_ADMIN").get();
                    roles.add(adminRole);
                } else {
                    Role userRole = roleService.findByName("ROLE_USER").get();
                    roles.add(userRole);
                }
            });
            oldUser.get().setRoles(roles);


            if (!user.getPassword().equals(oldUser.get().getPassword())) {
                oldUser.get().setPassword(user.getPassword());
                userService.save(oldUser.get());
            } else {
                userService.saveWithOutEncodePass(oldUser.get());
            }
            return new ResponseEntity<>(oldUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Search

    @GetMapping("/searchName/{keyword}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> searchName(@PathVariable(value = "keyword",required = false) String keyword) {
        return (List<User>) userService.searchName(keyword);
    }

    @GetMapping("/searchAddress/{keyword}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> searchAddress(@PathVariable("keyword") String keyword) {
        return (List<User>) userService.searchAddress(keyword);
    }

    @GetMapping("/searchAccountNumber/{accountNumber}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> searchAccountNumber(@PathVariable("accountNumber") int accountNumber) {
        return (List<User>) userService.searchAccountNumber(accountNumber);
    }

    @GetMapping("/searchBalanceG/{balance}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> searchBalanceG(@PathVariable("balance") int balance) {
        return (List<User>) userService.searchBalanceG(balance);
    }

    @GetMapping("/searchBalanceL/{balance}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> searchBalanceL(@PathVariable("balance") int balance) {
        return (List<User>) userService.searchBalanceL(balance);
    }

    @GetMapping("/searchEmployer/{employer}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<User> searchEmployer(@PathVariable("employer") String employer){
        return (List<User>) userService.searchEmployer(employer);
    }


}

