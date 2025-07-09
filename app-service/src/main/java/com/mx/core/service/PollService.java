package com.mx.core.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mx.core.common.exception.AppBadRequestException;
import com.mx.core.common.exception.ResourceNotFoundException;
import com.mx.core.model.payload.ChoiceResponse;
import com.mx.core.model.payload.PollRequest;
import com.mx.core.model.payload.PollResponse;
import com.mx.core.model.payload.UserSummary;
import com.mx.core.model.payload.VoteRequest;
import com.mx.core.repository.PollRepository;
import com.mx.core.repository.UserRepository;
import com.mx.core.repository.VoteRepository;
import com.mx.core.repository.entity.Choice;
import com.mx.core.repository.entity.ChoiceVoteCount;
import com.mx.core.repository.entity.Poll;
import com.mx.core.repository.entity.Usuario;
import com.mx.core.repository.entity.UsuarioPrincipal;
import com.mx.core.repository.entity.Vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class PollService {

	private final PollRepository pollRepository;
	private final VoteRepository voteRepository;
	private final UserRepository usuarioRepository;
	
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
    
    public PollResponse getPollById(Long pollId, UsuarioPrincipal currentUser) {    	
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
        Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));
        Usuario creator = usuarioRepository.findById(poll.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "id", poll.getCreatedBy()));
        Vote userVote = null;
        if(currentUser != null) {
            userVote = voteRepository.findByUserIdAndPollId(currentUser.getId(), pollId);
        }
        return this.mapPollToPollResponse(poll, choiceVotesMap,
                creator, userVote != null ? userVote.getChoice().getId(): null);
    }

    public PollResponse castVoteAndGetUpdatedPoll(Long pollId, VoteRequest voteRequest, UsuarioPrincipal currentUser) {
    	
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResourceNotFoundException("Poll", "id", pollId));

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            throw new AppBadRequestException("","","Sorry! This Poll has already expired");
        }

        Usuario user = usuarioRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        Choice selectedChoice = poll.getChoices().stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUsuario(user);
        vote.setChoice(selectedChoice);

        try {
        	
            vote = voteRepository.save(vote);
            
        } catch (DataIntegrityViolationException ex) {
            log.info("User {} has already voted in Poll {}", currentUser.getId(), pollId);
            throw new AppBadRequestException("","", "Sorry! You have already cast your vote in this poll");
        }

        List<ChoiceVoteCount> votes = voteRepository.countByPollIdGroupByChoiceId(pollId);
        Map<Long, Long> choiceVotesMap = votes.stream().collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        Usuario creator = usuarioRepository.findById(poll.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("User", "id", poll.getCreatedBy()));
        return this.mapPollToPollResponse(poll, choiceVotesMap, creator, vote.getChoice().getId());
        
    }
    
    public PollResponse mapPollToPollResponse(Poll poll, Map<Long, Long> choiceVotesMap, Usuario creator, Long userVote) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setQuestion(poll.getQuestion());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));
        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }
            return choiceResponse;
        }).collect(Collectors.toList());
        pollResponse.setChoices(choiceResponses);
        UserSummary creatorSummary = new UserSummary(creator.getId(), creator.getUsername(), creator.getName());
        pollResponse.setCreatedBy(creatorSummary);
        if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }
        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);
        return pollResponse;
    }
    
}
