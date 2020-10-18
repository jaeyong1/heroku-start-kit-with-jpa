package com.example.repository;

import com.example.entity.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> { //첫번째는 엔티티타입, 두번째는 @id 키타입을 뜻함
    Page<Team> findByName(String name, Pageable pageable); //구현체는 함수명보고 알아서 JPA가 해줌
//    Team findById(Long id);//기본정의

}
