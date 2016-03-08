package com.akmal.quickpoll.v3.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.akmal.quickpoll.domain.Poll;
import com.akmal.quickpoll.exception.ResourceNotFoundException;
import com.akmal.quickpoll.repository.PollRepository;


@RestController("pollControllerV3")
@RequestMapping("/v3/")
//@RequestMapping({"/v3/", "/oauth2/v3/"})
public class PollController {
	
	@Inject
	private PollRepository pollRepository;
	
	@RequestMapping(value = "/polls", method = RequestMethod.GET)
	public ResponseEntity<Page<Poll>> getAllPolls(Pageable pageable) {
		Page<Poll> allPolls = pollRepository.findAll(pageable);
		for(Poll p : allPolls) {
			updatePollResourceWithLinks(p, pageable);
		}
		return new ResponseEntity<>(allPolls, HttpStatus.OK);
	}
	
	/*@RequestMapping(value="/polls", method=RequestMethod.GET)
	public ResponseEntity<Iterable<Poll>> getAllPolls() {
		Iterable<Poll> allPolls = pollRepository.findAll();
		for(Poll p : allPolls) {
			updatePollResourceWithLinks(p);
		}
		return new ResponseEntity<>(allPolls, HttpStatus.OK);
	}*/
	
	@RequestMapping(value="/polls", method=RequestMethod.POST)
	public ResponseEntity<?> createPoll(@Valid @RequestBody Poll poll) {
		poll = pollRepository.save(poll);
		
		// Set the location header for the newly created resource
		HttpHeaders responseHeaders = new HttpHeaders();
		URI newPollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(poll.getId()).toUri();
		responseHeaders.setLocation(newPollUri);
		
		return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/polls/{pollId}", method=RequestMethod.GET)
	public ResponseEntity<?> getPoll(@PathVariable Long pollId) {
		verifyPoll(pollId);
		Poll p = pollRepository.findOne(pollId);
		updatePollResourceWithLinks(p, null);
		return new ResponseEntity<> (p, HttpStatus.OK);
	}
	
	@RequestMapping(value="/polls/{pollId}", method=RequestMethod.PUT)
	public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
		verifyPoll(pollId);
		// Save the entity
		Poll p = pollRepository.save(poll);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@RequestMapping(value="/polls/{pollId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
		verifyPoll(pollId);
		pollRepository.delete(pollId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	protected void verifyPoll(Long pollId) throws ResourceNotFoundException {
		Poll poll = pollRepository.findOne(pollId);
		if(poll == null) {
			throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
		}
	}
	
	private void updatePollResourceWithLinks(Poll poll, Pageable pageable) {
		poll.add(linkTo(methodOn(PollController.class).getAllPolls(pageable)).slash(poll.getPollId()).withSelfRel());
		poll.add(linkTo(methodOn(VoteController.class).getAllVotes(poll.getPollId())).withRel("votes"));
		poll.add(linkTo(methodOn(ComputeResultController.class).computeResult(poll.getPollId())).withRel("compute-result"));
	}
}
