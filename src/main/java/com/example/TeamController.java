package com.example;

import com.example.entity.Team;
import com.example.entity.TeamStatus;
import com.example.repository.TeamRepository;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/team")
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    //    WAS가 띄워질때 실행됨
    @PostConstruct
    public void init() {
        //Insert Test Data
        teamRepository.save(new Team("ATeam", TeamStatus.BAD));
        for (int i = 0; i < 30; i++) {
            teamRepository.save(new Team("BTeam", TeamStatus.GOOD));
        }
        for (int i = 0; i < 150; i++) {
            teamRepository.save(new Team("CTeam"));
        }
    }

    //[C] 데이터 추가
//    http://localhost:5000/team/create
//    Content-Type : application/json
//    {
//        "name": "AAAA",
//        "status": "GOOD"
//    }
    @RequestMapping("/create")
    public Long create(@RequestBody Team team) {//JSON 팀객체 받아옴
        System.out.println(team);
        Team saved = teamRepository.save(team);
        System.out.println(saved.getId());
        return saved.getId();
    }

    //[R] 프론트엔드에서 페이지정보 나눠서 요청
    //http://localhost:5000/team/list   (앞에서 20개, 디폴트)
    //http://localhost:5000/team/list?page=0&size=3&sort=id,DESC(기준적용)
    @Transactional(readOnly = true)
    @RequestMapping("/list")
    Page<Team> teams(Pageable pageable) {
        System.out.println("pageable:" + pageable);
//        return teamRepository.findAll(pageable).getContent(); // (결과리스트로 리턴)
        return teamRepository.findAll(pageable); //(결과+페이징정보 리턴)
    }

    //[R]백엔드에서 페이지기준 지정해서 검색
//    http://localhost:5000/team/findteam
    @RequestMapping("/findteam")
    List<Team> findTeam() {
        PageRequest request = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));//첫페이지가 0페이지, 페이지당 5개
        return teamRepository.findByName("BTeam", request).getContent();
    }

    //[R] Id로 한개 조회
//    http://localhost:5000/team/3
    @RequestMapping("/{id}")
    @Transactional(readOnly = true)
    Team findById(@PathVariable Long id) throws NotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        return team.orElseThrow(() -> new NotFoundException("id " + id + " not found"));
    }

    //[U] Partial update
//    http://localhost:5000/team/4/edit
//    Content-Type : application/json
//    {
//        "name": "new team name",
//        "status": "GOOD"
//    }
    @Transactional
    @RequestMapping("/{id}/update")
    Team updateTeam(@PathVariable Long id, @RequestBody Team team) throws NotFoundException {
        Team oldteam = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " not found"));
        JYPUtil.copyNonNullProperties(team, oldteam); //값복사
        teamRepository.save(oldteam);
        return oldteam;
    }

    //[D]
//    http://localhost:5000/team/4/delete
    @Transactional
    @RequestMapping("/{id}/delete")
    Team deleteTeam(@PathVariable Long id) throws NotFoundException {
        Team oldteam = teamRepository.findById(id).orElseThrow(() -> new NotFoundException("id " + id + " not found"));
        oldteam.setDeleted(true);
        teamRepository.save(oldteam);
        return oldteam;
    }


}

