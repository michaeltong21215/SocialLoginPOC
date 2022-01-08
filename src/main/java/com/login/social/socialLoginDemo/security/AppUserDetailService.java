package com.login.social.socialLoginDemo.security;

import com.login.social.socialLoginDemo.model.User;
import com.login.social.socialLoginDemo.model.UserDetailImpl;
import com.login.social.socialLoginDemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AppUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> user = userRepository.findByUsername(username);

       if(user.isEmpty()){
           throw new UsernameNotFoundException("user with username " + username + " is not found");
       }
       return UserDetailImpl.build(user.get());
    }
}
