package com.akmal.quickpoll.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

@Entity
public class Poll extends ResourceSupport {
	
	@Id
	@GeneratedValue
	@Column (name = "POLL_ID")
	private Long id;
	
	@Column (name = "QUESTION")
	@NotEmpty
	private String question;
	
	@OneToMany (cascade = CascadeType.ALL)
	@JoinColumn (name = "POLL_ID")
	@OrderBy
	@Size(min = 2, max = 6)
	private Set<Option> options;

	public Long getPollId() {
		return id;
	}

	public void setPollId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<Option> getOptions() {
		return options;
	}

	public void setOptions(Set<Option> options) {
		this.options = options;
	}
}
