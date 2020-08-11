package com.freegroupdevelopers.TamTamVoteBot.services;

import com.freegroupdevelopers.TamTamVoteBot.models.AnswerModel;
import com.freegroupdevelopers.TamTamVoteBot.models.UserModel;
import com.freegroupdevelopers.TamTamVoteBot.models.PollModel;
import com.freegroupdevelopers.TamTamVoteBot.repository.AnswerRepository;
import com.freegroupdevelopers.TamTamVoteBot.repository.UserRepository;
import com.freegroupdevelopers.TamTamVoteBot.repository.PollRepository;
import org.springframework.stereotype.Service;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 22.07.2020
 * (!_!)
 */

@Service
public class ServiceImpl {

    private final PollRepository pollRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public ServiceImpl(PollRepository pollRepository, UserRepository userRepository, AnswerRepository answerRepository) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public void saveUser(UserModel userModel) {
        userRepository.save(userModel);
    }

    public void saveUser(long userId, String userName) {
        if (userRepository.findById(userId).isPresent()) {
            return;
        }

        UserModel user = new UserModel();
        user.setId(userId);
        user.setUserName(userName);
        userRepository.save(user);
    }

    public void savePoll(long userId, PollModel pollModel) {
        UserModel user = userRepository.findById(userId).orElseThrow();

        if (pollModel.getAuthor() == null) {
            user.addVote(pollModel);
            pollModel.setAuthor(user);
            userRepository.save(user);
        }

        for (int i = 0; i < pollModel.getAnswers().size(); i++) {
            AnswerModel am = pollModel.getAnswers().get(i);

            if (answerRepository.findById(am.getId()).isEmpty()) {
                saveAnswer(pollModel.getAnswers().get(i));
            }
        }

        pollRepository.save(pollModel);
    }

    public void saveAnswer(AnswerModel answerModel) {
        answerRepository.save(answerModel);
    }

    public PollModel findPollBySessionId(String session) {
        return pollRepository.findBySession(session);
    }

    public PollModel findPollByMessageId(String messageId) {
        return pollRepository.findByMessageId(messageId);
    }

    public UserModel findUserById(long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
