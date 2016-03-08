package com.akmal.quickpoll.v3.controller;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.akmal.quickpoll.domain.Vote;
import com.akmal.quickpoll.dto.OptionCount;
import com.akmal.quickpoll.dto.VoteResult;
import com.akmal.quickpoll.repository.VoteRepository;


@RestController("computeResultControllerV3")
@RequestMapping("/v3/")
public class ComputeResultController {
	@Inject
	private VoteRepository voteRepository;

	
	@RequestMapping(value="/computeresult", method=RequestMethod.GET)
	public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
		VoteResult voteResult = new VoteResult();
		Iterable<Vote> allVotes = voteRepository.findByPoll(pollId);
		
		// Algorithm to count votes
		int totalVotes = 0;
		Map<Long, OptionCount> tempMap = new HashMap<Long, OptionCount>();
		for(Vote v : allVotes) {
			totalVotes ++;
			// Get the OptionCount corresponding to this Option
			OptionCount optionCount = tempMap.get(v.getOption().getId());
			if(optionCount == null) {
				optionCount = new OptionCount();
				optionCount.setOptionId(v.getOption().getId());
				tempMap.put(v.getOption().getId(), optionCount);
			}
			optionCount.setCount(optionCount.getCount()+1);
		}
		
		voteResult.setTotalVotes(totalVotes);
		voteResult.setResults(tempMap.values());
		
		return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
	}
}
