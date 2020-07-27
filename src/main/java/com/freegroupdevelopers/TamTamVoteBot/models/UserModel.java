package com.freegroupdevelopers.TamTamVoteBot.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 22.07.2020
 * (!_!)
 */

@Data
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @Column
    @Getter
    @Setter
    private long id;

    @NotNull
    @NotEmpty
    @Column
    @Getter
    @Setter
    private String userName;

    @ManyToMany
    private Set<PollModel> votedPolls;

    @ManyToMany
    private Set<AnswerModel> votedAnswers;

    @OneToMany(mappedBy="author")
    @OrderBy("creationTime DESC")
    @Getter
    private List<PollModel> votes;

    public void addVote(PollModel vote) {
        if (votes == null) {
            votes = new ArrayList<>();
        }

        votes.add(vote);
    }

    public void addVotedPoll(PollModel pollModel) {
        if (votedPolls == null) {
            votedPolls = new HashSet<>();
        }

        votedPolls.add(pollModel);
    }

    public void addVotedAnswer(AnswerModel answerModel) {
        if (votedAnswers == null) {
            votedAnswers = new HashSet<>();
        }

        votedAnswers.add(answerModel);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserModel))
            return false;

        return id == ((UserModel) o).id;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

}
