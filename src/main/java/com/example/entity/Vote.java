package com.example.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Vote {
    //접근키 1
    @Id
    private String key1;

    //접근키2
    @NotNull
    private String key2;

    //전화번호
    @NotNull
    private String telnum;

    //투표완료여부
    @NotNull
    private Boolean voted = false;

    //선택안(찬성=true)
    @NotNull
    private Boolean agreement = false;

    @UpdateTimestamp
    private LocalDateTime updated;



    //cons
    public Vote() {
    }

    public Vote(String key1, String key2, String telnum, Boolean voted, Boolean agreement) {
        this.key1 = key1;
        this.key2 = key2;
        this.telnum = telnum;
        this.voted = voted;
        this.agreement = agreement;
    }

    //get-set


    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getKey2() {
        return key2;
    }

    public void setKey2(String key2) {
        this.key2 = key2;
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public Boolean getVoted() {
        return voted;
    }

    public void setVoted(Boolean voted) {
        this.voted = voted;
    }

    public Boolean getAgreement() {
        return agreement;
    }

    public void setAgreement(Boolean agreement) {
        this.agreement = agreement;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    //to string
    @Override
    public String toString() {
        return "Vote{" +
                "key1='" + key1 + '\'' +
                ", key2='" + key2 + '\'' +
                ", telnum='" + telnum + '\'' +
                ", voted=" + voted +
                ", agreement=" + agreement +
                ", updated=" + updated +
                '}';
    }
}
