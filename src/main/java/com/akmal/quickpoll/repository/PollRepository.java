package com.akmal.quickpoll.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.akmal.quickpoll.domain.Poll;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {
	
}
