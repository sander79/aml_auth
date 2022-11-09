package it.sander.aml.domain.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import it.sander.aml.domain.model.User;
import it.sander.aml.domain.model.UserRole;

//@Profile("mock")
@Repository
public class UserRepositoryImplMock implements UserRepository {

	Map<String, User> userMap = new HashMap<String, User>();
	
    @Autowired
    private PasswordEncoder encoder;
	
    @PostConstruct
	public void init() {
        User adminUser = new User();
        adminUser.setEmail("admin@sanderaml.it");
        adminUser.setName("Admin");
        adminUser.setSurname("admin");
        adminUser.setPassword(encoder.encode("adminpassword"));
        adminUser.getRoles().addAll(List.of(UserRole.ADMIN, UserRole.READ_GPR, UserRole.WRITE_GPR));
        userMap.put(adminUser.getEmail(), adminUser);
        
        User sanderUser = new User();
        sanderUser.setEmail("sander@sanderaml.it");
        sanderUser.setName("Sander");
        sanderUser.setSurname("sander");
        sanderUser.setPassword(encoder.encode("sanderpassword"));
        sanderUser.getRoles().addAll(List.of(UserRole.READ_GPR));
        userMap.put(sanderUser.getEmail(), sanderUser);
	}

	@Override
	public Optional<User> findByEmail(String email) {	
		User user = userMap.get(email);
		if(user==null)
			return Optional.empty();
		
		return Optional.of(user);
	}

	@Override
	public void save(User adminUser) {
		// TODO Auto-generated method stub
		
	}

}
