package com.example;

import com.example.entity.Vote;
import com.example.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/vote")
//@RestController   //JSON타입으로 리턴
@SpringBootApplication //HTML파일 리턴
public class VoteController {

    @Autowired
    VoteRepository voteRepository;

    //    WAS가 띄워질때 실행됨
    @PostConstruct
    public void init() {
        //Insert Test Data
        //Ready
        voteRepository.save(new Vote("11111", "1111", "01011111111", false, false));
        voteRepository.save(new Vote("39283", "Ahr3", "01012345678", false, false));
        voteRepository.save(new Vote("76325", "gh4F", "01011112222", false, false));
        voteRepository.save(new Vote("56214", "h32A", "01033334444", false, false));
        voteRepository.save(new Vote("92235", "N4A3", "01055556666", false, false));

        //Voted
        voteRepository.save(new Vote("92235", "N4A3", "01077778888", true, true));
        voteRepository.save(new Vote("16712", "ALBE", "01099990000", true, true));
        voteRepository.save(new Vote("49187", "Ngg3", "01098765432", true, false));

    }


    @RequestMapping("/")
    String index() {
        return "registration";
    }


    @RequestMapping("/checkauth")
    String checkauth(@RequestParam(value = "key1") String key1,
                     @RequestParam(value = "key2") String key2,
                     @RequestParam(value = "telnum") String telnum,
                     HttpServletRequest req, //세션에 투표자 정보 저장용
                     Map<String, Object> model) {

        System.out.println("Try to User Login - Key1: " + key1 + ", Key2: " + key2 + ", Telnum(4 digit): " + telnum);

        Optional<Vote> voter = voteRepository.findById(key1);
        System.out.println(
                "Login Info :" + voter.orElse(new Vote()).toString()
        );

        //check validation
        if (!voter.isPresent()) {
            System.out.println("Login Auth Failed. Key1: " + key1);
            model.put("errmsg", "로그인정보가 잘못되었습니다.");
            return "voteerror";

        } else if (voter.get().getVoted() == true) {
            System.out.println("already voted. Key1: " + key1);
            model.put("errmsg", "이미 투표를 완료하였습니다.(" + voter.get().getUpdated() + ")");
            return "voteerror";

        } else {
            String DBtelnum = voter.get().getTelnum();
            String DBtelnum4 = DBtelnum.substring(DBtelnum.length() - 4, DBtelnum.length());
            if (!DBtelnum4.equals(telnum)) {
                System.out.println("Not matching telnum 4 digits. Key1: " + key1);
                model.put("errmsg", "입력된 전화번호(끝4자리)가 일치하지 않습니다.");
                return "voteerror";
            }

            System.out.println("Login Success. Key1: " + key1);
        }

        // 세션 선언
        HttpSession session = req.getSession();
        // 사용자 session 저장
        session.setAttribute("voterInfo", voter.get());
        return "voting";
    }


    //    //for test
    @RequestMapping("/votingtest")
    String voting(Map<String, Object> model, HttpServletRequest req) {
        Optional<Vote> voter = voteRepository.findById("11111");
        System.out.println(
                "Login Info :" + voter.orElse(new Vote()).toString()
        );

        // 세션 선언
        HttpSession session = req.getSession();
        // 사용자 session 저장
        session.setAttribute("voterInfo", voter.get());

        return "voting";
    }

    @RequestMapping("/saveagreement")
    String saveAgreement(HttpServletRequest req, Map<String, Object> model,
                         @RequestParam(value = "agreement") String agreement
    ) {

        try {
            // 사용자 session 가져오기
            HttpSession session = req.getSession();
            Vote sess_voter = (Vote) session.getAttribute("voterInfo");
            System.out.println("[saveagreement] 투표자 아이디 :: " + sess_voter.getKey1());
            System.out.println("[saveagreement] 세션 아이디 :: " + session.getId());
            System.out.println("[saveagreement] agreement: " + agreement);


            Optional<Vote> voter = voteRepository.findById(sess_voter.getKey1());

            if (!voter.isPresent()) {
                System.out.println("Login Auth Failed. Key1: " + sess_voter.getKey1());
                model.put("errmsg", "잘못된 접근. 로그인정보가 잘못되었습니다.");
                return "voteerror";
            } else if (voter.get().getVoted() == true) {
                System.out.println("already voted. Key1: " + sess_voter.getKey1());
                model.put("errmsg", "이미 투표를 완료하였습니다.(" + voter.get().getUpdated() + ")");
                return "voteerror";
            } else {
                System.out.println("Login Success. Key1: " + sess_voter.getKey1());
            }


            //투표선택결과 저장
            if (agreement.equals("true")) {
                voter.get().setVoted(true);
                voter.get().setAgreement(true);
                voteRepository.save(voter.get());
                voteRepository.flush();
            } else if (agreement.equals("false")) {
                voter.get().setVoted(true);
                voter.get().setAgreement(false);
                voteRepository.save(voter.get());
                voteRepository.flush();
            } else {//null로 남아있음.
                model.put("errmsg", "정상적인 선택과정이 아닙니다.");
                return "voteerror";
            }


            session.invalidate(); //세션만료처리
        } catch (Exception e) {
            System.out.println("[saveagreement] Session loading Failure. Try-catch error.");
            model.put("errmsg", "로그인 세션정보를 찾을 수 없습니다.");
            return "voteerror";
        }

        return "redirect:result";
    }


    @RequestMapping("/result")
    String result(Map<String, Object> model) {
        model.put("nVoted", voteRepository.countByVoted(true) + "표"); //현재까지 투표인원 수
        model.put("nKeys", "(발행투표권 수 : " + voteRepository.count() + ")");
        model.put("nAgreed", voteRepository.countByVotedAndAgreement(true, true) + "표"); //찬성 득표수
        model.put("nNotAgreed", voteRepository.countByVotedAndAgreement(true, false) + "표"); //반대 득표수
        return "result";
    }

}

