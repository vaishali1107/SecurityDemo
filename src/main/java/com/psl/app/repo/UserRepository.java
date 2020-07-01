package com.psl.app.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.psl.app.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

	public User findByUsername(String username);
}
