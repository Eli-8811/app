package com.mx.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mx.core.repository.entity.ChoiceVoteCount;
import com.mx.core.repository.entity.Vote;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
	
    @Query("SELECT NEW com.mx.core.repository.entity.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id in :pollIds GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPollIdInGroupByChoiceId(@Param("pollIds") List<Long> pollIds);

    @Query("SELECT NEW com.mx.core.repository.entity.ChoiceVoteCount(v.choice.id, count(v.id)) FROM Vote v WHERE v.poll.id = :pollId GROUP BY v.choice.id")
    List<ChoiceVoteCount> countByPollIdGroupByChoiceId(@Param("pollId") Long pollId);

    @Query("SELECT v FROM Vote v where v.usuario.id = :userId and v.poll.id in :pollIds")
    List<Vote> findByUserIdAndPollIdIn(@Param("userId") Long userId, @Param("pollIds") List<Long> pollIds);

    @Query("SELECT v FROM Vote v where v.usuario.id = :userId and v.poll.id = :pollId")
    Vote findByUserIdAndPollId(@Param("userId") Long userId, @Param("pollId") Long pollId);

    @Query("SELECT COUNT(v.id) from Vote v where v.usuario.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT v.poll.id FROM Vote v WHERE v.usuario.id = :userId")
    Page<Long> findVotedPollIdsByUserId(@Param("userId") Long userId, Pageable pageable);
    
}
