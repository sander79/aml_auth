package it.sander.aml.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import it.sander.aml.domain.model.User;

@Repository
public interface UserRepository {

    Optional<User> findByEmail(String email);

	void save(User adminUser);

}
