package com.example.demo.controller;
import com.example.demo.Jwt.JwtResponse;
import com.example.demo.model.entities.Role;
import com.example.demo.model.entities.User;
import com.example.demo.model.service.JwtService;
import com.example.demo.model.service.RoleService;
import com.example.demo.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.HashSet;
import java.util.Set;

@CrossOrigin("*")
@RestController
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private RoleService roleService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(user.getUsername()).get();
        return ResponseEntity.ok(new JwtResponse(jwt, currentUser.getId(), userDetails.getUsername(), userDetails.getAuthorities()));
    }

//    @PostMapping("/test")
//    public boolean saveData(@RequestBody Set<User> userSet){
//        try{
//            Set<Role> roles = new HashSet<>();
//            Role userRole = roleService.findById(2).get();
//            roles.add(userRole);
//            for (User u:userSet) {
//                u.setUsername(u.getEmail());
//                u.setRoles(roles);
//                u.setPassword("123456");
//            }
//            userService.saveAll(userSet);
//            return true;
//        }catch (Exception e){
//            return false;
//        }
//
//    }
}
