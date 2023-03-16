package com.myblog.blogapp.controller;

import com.myblog.blogapp.entities.Role;
import com.myblog.blogapp.entities.User;
import com.myblog.blogapp.payload.JWTAuthResponse;
import com.myblog.blogapp.payload.LoginDto;
import com.myblog.blogapp.payload.SignUpDto;
import com.myblog.blogapp.repository.RoleRepository;
import com.myblog.blogapp.repository.UserRepository;
import com.myblog.blogapp.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;           //bean creation is required of authenticationManager in SecurityConfig
    @Autowired
    private UserRepository userRepository;                         //bean creation for using custom methods of userRepository
    @Autowired
    private PasswordEncoder passwordEncoder;                       //bean creation for encrypting the password
    @Autowired
    private RoleRepository roleRepository;                         //bean creation for finding role
    @Autowired
    private JwtTokenProvider tokenProvider;                        //bean creation helps us to generate token


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){  //authenticationManager is a builtin class which verify username,password is correct or not in db.
        Authentication authentication = authenticationManager.authenticate(                   //if verify successfully,then run further, otherwise stops here.
                new UsernamePasswordAuthenticationToken                                       //authenticate(obj. of authentication and
                        (loginDto.getUsernameOrEmail(),                                               //in this we supply username, password)
                                loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);                 //it will set authenticationToken, if authenticateUser() is true.

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);                           //calling generateToken()
        return ResponseEntity.ok(new JWTAuthResponse(token));                                 //it will return token to PostMan
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        // check for username exists in a DB
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);      //if exist
        }
        // check for email exists in DB
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);         //if exist
        }
        // create user object, for coping signup content to user(name,username,email,password,roles)
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));         //encrypt the password
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();                //go to role table and find role(name) //here role is always admin
        user.setRoles(Collections.singleton(roles));                               //convert role obj. into set  (Singleton:when we give obj. address, it will store as set)

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
