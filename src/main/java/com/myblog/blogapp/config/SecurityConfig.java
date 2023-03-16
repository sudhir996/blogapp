package com.myblog.blogapp.config;

import com.myblog.blogapp.repository.UserRepository;
import com.myblog.blogapp.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)                                        //Role define in PostController
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired                                                                            //when variable has to create obj.(@Autowired) used
    private CustomUserDetailsService userDetailsService;                                  //it has loadByUserName() : security package

    @Bean                                                                                 //when developing a method has to create obj.(@Bean) used.
    PasswordEncoder passwordEncoder(){                                                    //created obj. of BCryptPasswordEncoder
        return new BCryptPasswordEncoder();                                               //and inject into PasswordEncoder
    }


    @Override
    @Bean                                                                                 //create obj. and inject into authenticationManager
    protected AuthenticationManager authenticationManager() throws Exception {            //ctrl+O (for LoginDto)
        return super.authenticationManager();
    }


    @Override //1
    protected void configure(HttpSecurity http) throws Exception {                         //ctrl+O  //here we configure which url is access by whom
        http                                                                               //hcd4ah(Basic Authentication)
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()              //here we control which URL, who can access, permit to all(for InMemoryAuthentication)
                .antMatchers("/api/auth/**").permitAll()                         //signin page is open for all
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {         //create bean, this method helps us to deal with database
        auth.userDetailsService(userDetailsService)                                        //userDetailsService() has consists of Email,Password, Role i.eGrantedAuthority
             .passwordEncoder(passwordEncoder());                                          //it will encrypt password
    }

//2   @Override (InMemoryAuthentication Concept)
//    @Bean
//    protected UserDetailsService userDetailsService() {                                              //In memory Authentication(not in db), it helps us to remove authentication from
//        UserDetails ramesh = User.builder().username("ramesh").password(passwordEncoder()            //application.properties and we can create user,password,roles here.
//                .encode("password")).roles("USER").build();
//        UserDetails admin = User.builder().username("admin").password(passwordEncoder()              //(passwordEncoder().encode("admin")) will encrypt the password in 64bit
//                .encode("admin")).roles("ADMIN").build();                                            //.encode() is a builtin method
//        return new InMemoryUserDetailsManager(ramesh, admin);
//    }

}


