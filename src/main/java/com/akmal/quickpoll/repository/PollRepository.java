package com.akmal.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.akmal.quickpoll.domain.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> {
	
}
