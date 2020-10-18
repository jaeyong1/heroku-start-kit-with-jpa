package com.example.entity;


import com.sun.istack.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Where(clause="deleted=false")
public class Team {
    @Id
    @GeneratedValue
    private Long id;

//    @NotNull
    private String name;

    @Enumerated(EnumType.STRING)
    private TeamStatus status;

    @CreationTimestamp
    @Column(insertable=true, updatable=false)
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    //Soft-delete
    private Boolean deleted = false;

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    //    JPA는 Default construct 필요 - (생성자를 구현안할꺼면 하나도 없던가. 다른 construct를 만들었다면 기본생성자 필요)
    public Team() {
    }

    public Team(String name) {
        setName(name);
    }

    public Team(String name, TeamStatus status) {
        setName(name);
        setStatus(status);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
    }


    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
