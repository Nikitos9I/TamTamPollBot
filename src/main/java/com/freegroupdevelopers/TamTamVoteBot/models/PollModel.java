package com.freegroupdevelopers.TamTamVoteBot.models;

import chat.tamtam.botapi.model.*;
import com.freegroupdevelopers.TamTamVoteBot.support.text.Text;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 22.07.2020
 * (!_!)
 */

@Data
@Entity
@Table(name = "poll")
public class PollModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Getter
    @Setter
    private long id;

    @Getter
    @Setter
    @Column
    private Boolean isPublic;

    @Getter
    @Setter
    @Column
    private Boolean isMultiVote;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private String locale;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private String session;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private Integer votesToShow;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private Integer votesCount;

    @Getter
    @Setter
    @Column
    private String messageId;

    @NotNull
    @NotEmpty
    @Getter
    @Setter
    @Column
    private String question;

    @CreationTimestamp
    @Getter
    @Setter
    @Column
    private Date creationTime;

    @OneToMany(mappedBy="pollModel")
    @OrderBy("creationTime")
    private List<AnswerModel> answers;

    @ManyToMany(mappedBy= "votedPolls")
    private Set<UserModel> votedUsers;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @Getter
    @Setter
    private UserModel author;

    public List<AnswerModel> getAnswers() {
        if (answers == null) {
            answers = new ArrayList<>();
        }

        return answers;
    }

    public void addAnswer(AnswerModel answer) {
        if (answers == null) {
            answers = new ArrayList<>();
        }

        answers.add(answer);
    }

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

    public NewMessageBody getMessage() {
        if (question.isEmpty()) {
            return new NewMessageBody(null, null, null);
        }

        List<Button> buttons = new ArrayList<>();

        for (int i = 0; i < answers.size(); ++i) {
            AnswerModel e = answers.get(i);
            buttons.add(new CallbackButton(e.getPayload(), Text.getPrefix(i + 1)).intent(Intent.POSITIVE));
        }

        InlineKeyboardAttachmentRequestPayload keyboardAttachmentRequest =
                new InlineKeyboardAttachmentRequestPayload(Collections.singletonList(buttons));
        AttachmentRequest attachmentRequest = new InlineKeyboardAttachmentRequest(keyboardAttachmentRequest);

        String mText = getMessageText();
        return new NewMessageBody(mText, Collections.singletonList(attachmentRequest), null);
    }

    public String getMessageText() {
        StringBuilder res = new StringBuilder();
        res.append(Text.text(locale).pollType(isPublic)).append(question);
        res.append(System.lineSeparator()).append(System.lineSeparator());

        for (int i = 0; i < answers.size(); ++i) {
            AnswerModel e = answers.get(i);
            double counted = e.getVotesCount() * 1.0 / Integer.max(votesCount, 1) * 100;

            res.append(Text.getPrefix(i + 1)).append(" ");
            res.append(Math.round(counted)).append("% — ");
            res.append(e.getAnswer()).append(System.lineSeparator());

            if (isPublic) {
                String votedUsers = e.getVotedUsers().stream()
                        .map(u -> u.getUserName() == null? "anonymous" : "@" + u.getUserName())
                        .collect(Collectors.joining(", "));
                res.append(votedUsers);

                if (!votedUsers.isEmpty()) {
                    res.append(System.lineSeparator());
                }
            }
        }

        if (answers.size() > 0)
            res.append(System.lineSeparator()).append(Text.text(locale).globalCount(votesToShow));

        return res.toString();
    }

    public boolean needQuestion() {
        return question == null || question.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PollModel))
            return false;

        return id == ((PollModel) o).id;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

}
