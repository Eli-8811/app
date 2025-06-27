package com.mx.core.service;

import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.mx.core.model.payload.PollRequest;
import com.mx.core.repository.PollRepository;
import com.mx.core.repository.entity.Choice;
import com.mx.core.repository.entity.Poll;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PollService {

	private final PollRepository pollRepository;
	
    public Poll createPoll(PollRequest pollRequest) {
    	
        Poll poll = new Poll();
        
        poll.setQuestion(pollRequest.getQuestion());

        pollRequest.getChoices().forEach(choiceRequest -> {
            poll.addChoice(new Choice(choiceRequest.getText()));
        });

        Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
                .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));

        poll.setExpirationDateTime(expirationDateTime);

        return pollRepository.save(poll);
        
    }
    
}
