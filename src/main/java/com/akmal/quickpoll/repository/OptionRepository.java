package com.akmal.quickpoll.repository;

import org.springframework.data.repository.CrudRepository;

import com.akmal.quickpoll.domain.Option;

public interface OptionRepository extends CrudRepository<Option, Long> {
	
}
