package com.freegroupdevelopers.TamTamVoteBot.actions;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.model.*;
import com.freegroupdevelopers.TamTamVoteBot.models.AnswerModel;
import com.freegroupdevelopers.TamTamVoteBot.models.PollModel;
import com.freegroupdevelopers.TamTamVoteBot.models.UserModel;
import com.freegroupdevelopers.TamTamVoteBot.services.ServiceImpl;
import com.freegroupdevelopers.TamTamVoteBot.support.BotKeyboard;
import com.freegroupdevelopers.TamTamVoteBot.support.BotLogger;
import com.freegroupdevelopers.TamTamVoteBot.support.text.Text;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 20.07.2020
 * (!_!)
 */

@Component
public class Process {

    private final TamTamBotAPI client;
    private final Logger logger;
    private final ServiceImpl service;

    public Process(TamTamBotAPI client, BotLogger logger, ServiceImpl service) {
        this.client = client;
        this.logger = logger.getLogger(Process.class);
        this.service = service;
    }

    private void makeInitialConstruction(ConstructorAnswer constructorAnswer, PollModel pm) {
        constructorAnswer
                .hint(Text.text().choosePoll())
                .allowUserInput(false)
                .keyboard(BotKeyboard.getInitialKeyboard());
    }

    private void makeQuestionConstruction(ConstructorAnswer constructorAnswer) {
        String _text = Text.text().questionMessage();
        constructorAnswer
                .hint(_text)
                .allowUserInput(true);
    }

    private void makeAnswerConstruction(ConstructorAnswer constructorAnswer, PollModel pm) {
        int answerNumber = pm.getAnswers().size() + 1;
        String _text = Text.text().answerMessage(answerNumber);

        if (pm.getAnswers().size() == 0) {
            String message = _text + Text.text().yourQuestion() + pm.getQuestion();
            constructorAnswer
                    .allowUserInput(true)
                    .hint(message)
                    .placeholder(_text);
        } else {
            constructorAnswer
                    .allowUserInput(answerNumber <= 8)
                    .placeholder(_text)
                    .messages(Collections.singletonList(pm.getMessage()));
        }
    }

    private ConstructorAnswer makeCurrentConstruction(PollModel pm) {
        ConstructorAnswer constructorAnswer = new ConstructorAnswer();

        if (pm.getIsPublic() == null) {
            makeInitialConstruction(constructorAnswer, pm);
            return constructorAnswer;
        }

        if (pm.needQuestion()) {
            makeQuestionConstruction(constructorAnswer);
        } else {
            makeAnswerConstruction(constructorAnswer, pm);
        }

        return constructorAnswer;
    }

    private void updateVoteFlow(PollModel pm, String userField) {
        if (pm.needQuestion()) {
            pm.setQuestion(userField);
        } else {
            AnswerModel am = new AnswerModel();
            UUID uuid = UUID.randomUUID();
            am.setVotesCount(0);
            am.setAnswer(userField);
            am.setPayload(uuid.toString());
            am.setPollModel(pm);
            pm.addAnswer(am);
        }
    }

    public void sendNewConstruction(String sessionId, ConstructorInput constructorInput, long userId) {
        PollModel pm = service.findPollBySessionId(sessionId);

        if (pm == null) {
            logger.info("New poll created");
            pm = new PollModel();
            pm.setSession(sessionId);
        }

        if (constructorInput instanceof MessageConstructorInput) {
            List<NewMessageBody> messages = ((MessageConstructorInput) constructorInput).getMessages();

            if (messages == null || messages.isEmpty()) {
                logger.warn("Messages list is empty or null");
                ConstructorAnswer constructorAnswer = makeCurrentConstruction(pm);
                sendConstructionMessage(constructorAnswer, sessionId);
                service.savePoll(userId, pm);
                return;
            }

            updateVoteFlow(pm, messages.get(0).getText());
            service.savePoll(userId, pm);
            ConstructorAnswer constructorAnswer = makeCurrentConstruction(pm);
            sendConstructionMessage(constructorAnswer, sessionId);
        } else {
            String payload = ((CallbackConstructorInput) constructorInput).getPayload();
            logger.info("Setup poll to " + payload + " type");

            if (payload.equals("public")) {
                pm.setIsPublic(true);
            } else if (payload.equals("anon")) {
                pm.setIsPublic(false);
            }

            ConstructorAnswer constructorAnswer = makeCurrentConstruction(pm);
            sendConstructionMessage(constructorAnswer, sessionId);
            service.savePoll(userId, pm);
        }
    }

    public void updatePublishedVote(String payload, String messageId, User user) {
        PollModel pm = service.findPollByMessageId(messageId);
        UserModel voter = service.findUserById(user.getUserId());

        if (pm == null) {
            logger.warn("Can not look up poll with message id: " + messageId);
            return;
        }

        if (voter == null) {
            logger.info("New user wants to put vote");
            voter = new UserModel();
            voter.setId(user.getUserId());
            voter.setUserName(user.getUsername());
        }

        if (pm.getVotedUsers().contains(voter)) {
            return;
        }

        AnswerModel am = pm.getAnswers().stream().filter(e -> e.getPayload().equals(payload)).findAny().orElseThrow();
        am.setVotesCount(am.getVotesCount() + 1);
        am.addVotedUser(voter);
        pm.setVotesCount(pm.getVotesCount() + 1);
        pm.addVotedUser(voter);
        voter.addVotedPoll(pm);
        voter.addVotedAnswer(am);

        service.saveUser(voter);
        service.saveAnswer(am);
        service.savePoll(pm.getAuthor().getId(), pm);

        NewMessageBody mb = new NewMessageBody(pm.getMessageText(), null, null);
        updateMessage(mb, messageId);
    }

    public void publishPoll(String sessionId, String messageId, long senderId) {
        PollModel pm = service.findPollBySessionId(sessionId);

        if (pm == null) {
            logger.warn("Can not look up poll with session id: " + sessionId);
            return;
        }

        pm.setMessageId(messageId);
        service.savePoll(senderId, pm);
    }

    public void sayHello(long userId) {
        String _text = Text.text().helloMessage();
        NewMessageBody message = new NewMessageBody(_text, null, null);

        sendMessage(message, userId);
    }

    public void sendDescMessage(long userId) {
        String _text = Text.text().descriptionMessage();
        NewMessageBody message = new NewMessageBody(_text, null, null);

        sendMessage(message, userId);
    }

    private void sendMessage(NewMessageBody messageBody, long userId) {
        try {
            client.sendMessage(messageBody)
                    .userId(userId)
                    .execute();
        } catch (ClientException | APIException e) {
            System.out.println(e);
            //TODO: handle exceptions
        }
    }

    private void sendConstructionMessage(ConstructorAnswer constructorAnswer, String sessionId) {
        try {
            client.construct(constructorAnswer, sessionId).execute();
        } catch (APIException | ClientException e) {
            System.out.println(e);
            //TODO: handle exceptions
        }
    }

    private void updateMessage(NewMessageBody messageBody, String messageId) {
        try {
            client.editMessage(messageBody, messageId).execute();
        } catch (APIException | ClientException e) {
            System.out.println(e);
            //TODO: handle exceptions
        }
    }

}
