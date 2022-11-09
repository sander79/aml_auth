package it.sander.aml.domain.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import it.sander.aml.application.security.JwtProvider;
import it.sander.aml.domain.model.User;
import it.sander.aml.domain.model.UserRole;
import it.sander.aml.domain.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    
    private ObjectWriter ow = new ObjectMapper().writer();

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password)  {
        User user = this.userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        
        String userJson = null;
		try {
			userJson = ow.writeValueAsString(mapUser(user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        Map<String, String> claimMap = new HashMap<>(0);
        claimMap.put("user", userJson);
        return JwtProvider.createJwt(email, claimMap);
    }
    
    private org.springframework.security.core.userdetails.User mapUser(User user) {
    	Collection<GrantedAuthority> auth = new LinkedList<GrantedAuthority>();
    	
    	for(UserRole ddd : user.getRoles()) {
    		auth.add(new SimpleGrantedAuthority(ddd.name()));	
    	}
		return new org.springframework.security.core.userdetails.User(user.getName(), "", auth);
    }
}
