package com.example.repository;

import com.example.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, String> { //첫번째는 엔티티타입, 두번째는 @id 키타입을 뜻함
//    Team findById(Long id);//기본정의
    long count();
    long countByVoted(Boolean voted);
    long countByVotedAndAgreement(Boolean voted, Boolean agreement);
}
