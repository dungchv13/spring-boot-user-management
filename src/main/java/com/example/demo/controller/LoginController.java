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

    @Autowired
    private RoleService roleService;


//    @PostMapping("/register")
//    public ResponseEntity registerUser(@RequestBody User user) {
//        if(userService.existsByUsername(user.getUsername())) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        user.setAvatar("https://i1.wp.com/www.mnleadership.org/wp-content/uploads/2017/02/Anonymous-Avatar.png?ssl=1");
//        User newUser = getNewUser(user);
//        userService.save(newUser);
//        return new ResponseEntity(HttpStatus.OK);
//    }

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

    @PostMapping("/test")
    public boolean saveData(@RequestBody Set<User> userSet){
        try{
            Set<Role> roles = new HashSet<>();
            Role userRole = roleService.findById(2).get();
            roles.add(userRole);
            for (User u:userSet) {
                u.setUsername(u.getEmail());
                u.setRoles(roles);
                u.setPassword("123456");
            }
            userService.saveAll(userSet);
            return true;
        }catch (Exception e){
            return false;
        }

    }

//    private User getNewUser(User user) {
//        User newUser = new User();
//        newUser.setUsername(user.getUsername());
//        newUser.setPassword(user.getPassword());
//        newUser.setFirstName(user.getFirstName());
//        newUser.setLastName(user.getLastName());
//        newUser.setAddress(user.getAddress());
//        newUser.setAvatar(user.getAvatar());
//        newUser.setBlocked(false);
//        Date date = new Date();
//        Timestamp created_date = new Timestamp(date.getTime());
//        newUser.setCreatedDate(created_date);
//        newUser.setDateOfBirth(user.getDateOfBirth());
//        newUser.setDetail(user.getDetail());
//        newUser.setEmail(user.getEmail());
//        newUser.setGender(user.getGender());
//        newUser.setGender(user.getPhone());
//        Set<Role> roles = new HashSet<>();
//        user.getRoles().forEach(role -> {
//            if (role.getName().equals("admin")) {
//                Role adminRole = roleService.findByName("ROLE_ADMIN").get();
//                roles.add(adminRole);
//            } else {
//                Role userRole = roleService.findByName("ROLE_USER").get();
//                roles.add(userRole);
//            }
//        });
//        newUser.setRoles(roles);
//        return newUser;
//    }
}
