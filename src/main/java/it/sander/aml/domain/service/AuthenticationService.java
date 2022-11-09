package it.sander.aml.domain.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import it.sander.aml.application.security.JwtProvider;
import it.sander.aml.domain.model.User;
import it.sander.aml.domain.model.UserRole;
import it.sander.aml.domain.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
 
    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password)  {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        
        return JwtProvider.createJwt(email, mapUserRoles(user));
    }
    
    private List<String> mapUserRoles(User user) {
    	List<String> roles = new LinkedList<String>();
    	
    	for(UserRole ddd : user.getRoles()) {
    		roles.add(ddd.name());	
    	}
		return roles;
    }
}
