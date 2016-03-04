package com.akmal.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.akmal.quickpoll.domain.User;


public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findByUsername(String username);
}
