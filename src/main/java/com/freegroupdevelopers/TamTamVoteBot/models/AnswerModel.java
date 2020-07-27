package com.freegroupdevelopers.TamTamVoteBot.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 22.07.2020
 * (!_!)
 */

@Data
@Entity
@Table(name = "answer")
public class AnswerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Getter
    @Setter
    private long id;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private String answer;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private String payload;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private int votesCount;

    @CreationTimestamp
    @Getter
    @Setter
    @Column
    private Date creationTime;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "poll_id", referencedColumnName = "id")
    @Getter
    @Setter
    private PollModel pollModel;

    @ManyToMany(mappedBy= "votedAnswers")
    private Set<UserModel> votedUsers;

    public Set<UserModel> getVotedUsers() {
        if (votedUsers == null) {
            votedUsers = new HashSet<>();
        }

        return votedUsers;
    }

    public void addVotedUser(UserModel userModel) {
        if (votedUsers == null) {
            votedUsers = new HashSet<>();
        }

        votedUsers.add(userModel);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AnswerModel))
            return false;

        return id == ((AnswerModel) o).id;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

}
