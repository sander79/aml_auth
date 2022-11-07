package it.sander.aml.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.sander.aml.domain.model.User;
import it.sander.aml.domain.model.UserRole;
import it.sander.aml.repository.UserRepository;

@Component
@ConfigurationProperties(prefix = "admin")
public class StartUpService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private String email;
    private String password;

    @PostConstruct
    public void initAdmin() {
        if (this.userRepository.findByEmail(this.email).isEmpty()) {
            User adminUser = new User();
            adminUser.setEmail(this.email);
            adminUser.setName("Admin");
            adminUser.setSurname("Admin");
            adminUser.setPassword(encoder.encode(this.password));
            adminUser.getRoles().add(UserRole.ADMIN);
            this.userRepository.save(adminUser);
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
