package com.myblog.blogapp.security;

import com.myblog.blogapp.entities.Role;
import com.myblog.blogapp.entities.User;
import com.myblog.blogapp.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;                //builtin import
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {      //implementing becoz loadUserByUsername
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {       //bean of userRepository
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {                     //ctrl+O (1 method only : Functional Interface)
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)                               //findByUsernameOrEmail, whether username
                .orElseThrow(() ->                                                                                       //or Email is same or not   //give user obj.
                        new UsernameNotFoundException("User not found with username or email:" + usernameOrEmail));      //builtin feature for exception

        return new org.springframework.security.core.userdetails.User(                                                   //builtin-User(Email,Password,Role)
                user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles())                                //map Roles to Authorities         //convert Roles into Set
        );
    }

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){               //creating mapRolesToAuthorities method to convert All roles as Set
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());      //convert roles to SimpleGrantedAuthority obj.
    }
}