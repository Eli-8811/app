package com.mx.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mx.core.controller.security.CurrentUser;
import com.mx.core.model.ResponseGeneric;
import com.mx.core.model.payload.PollRequest;
import com.mx.core.model.payload.PollResponse;
import com.mx.core.model.payload.VoteRequest;
import com.mx.core.repository.entity.Poll;
import com.mx.core.repository.entity.UsuarioPrincipal;
import com.mx.core.service.PollService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/poll")
@AllArgsConstructor
public class PollController {

	private final PollService pollService;
	
	@PostMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResponseGeneric<?>> createPoll(@Valid @RequestBody PollRequest pollRequest) {
	    try {
	        Poll poll = pollService.createPoll(pollRequest);
	        return ResponseEntity.ok(ResponseGeneric.buildSuccess("Encuesta creada correctamente", poll));
	    } catch (RuntimeException e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseGeneric.buildError("Error al crear la encuesta"));
	    }
	}
	
    @GetMapping("/{pollId}")
    @PreAuthorize("hasRole('USER')")
    public PollResponse getPollById(@CurrentUser UsuarioPrincipal currentUser,
                                    @PathVariable Long pollId) {
        return pollService.getPollById(pollId, currentUser);
    }
    
    @PostMapping("/{pollId}/votes")
    @PreAuthorize("hasRole('USER')")
    public PollResponse castVote(
    			@CurrentUser UsuarioPrincipal currentUser,
                         @PathVariable Long pollId,
                         @Valid @RequestBody VoteRequest voteRequest) {
    	
        return pollService.castVoteAndGetUpdatedPoll(pollId, voteRequest, currentUser);
        
    }
    
}
