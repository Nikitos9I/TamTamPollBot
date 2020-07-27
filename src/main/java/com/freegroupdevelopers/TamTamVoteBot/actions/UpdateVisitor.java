package com.freegroupdevelopers.TamTamVoteBot.actions;

import chat.tamtam.botapi.model.*;
import com.freegroupdevelopers.TamTamVoteBot.services.ServiceImpl;
import com.freegroupdevelopers.TamTamVoteBot.support.BotLogger;
import com.freegroupdevelopers.TamTamVoteBot.support.Config;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 20.07.2020
 * (!_!)
 */

@Component
public class UpdateVisitor implements Update.Visitor {

    private final Logger logger;
    private final Process process;
    private final ServiceImpl service;

    public UpdateVisitor(BotLogger logger, Process process, ServiceImpl service) {
        this.logger = logger.getLogger(UpdateVisitor.class);
        this.process = process;
        this.service = service;
    }

    @Override
    public void visit(MessageCreatedUpdate model) {
        Config.setLocale(model.getUserLocale());
        long userId = model.getMessage().getSender().getUserId();

        process.sendDescMessage(userId);
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        String mId = Objects.requireNonNull(model.getMessage()).getBody().getMid();
        String payload = model.getCallback().getPayload();
        User user = model.getCallback().getUser();
        process.updatePublishedVote(payload, mId, user);
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        logger.info("I AM in MessageEditedUpdate");
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        logger.info("I AM in MessageRemovedUpdate");
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        logger.info("I AM in BotAddedToChatUpdate");
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        logger.info("I AM in BotRemovedFromChatUpdate");
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        logger.info("I AM in UserAddedToChatUpdate");
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        logger.info("I AM in UserRemovedFromChatUpdate");
    }

    @Override
    public void visit(BotStartedUpdate model) {
        Config.setLocale(model.getUserLocale());
        User user = model.getUser();
        long userId = user.getUserId();

        service.saveUser(user.getUserId(), user.getUsername());
        process.sayHello(userId);

        logger.info("Bot was added by user: " + user.getUserId());
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {

    }

    @Override
    public void visit(MessageConstructionRequest model) {
        Config.setLocale(model.getUserLocale());
        String sessionId = model.getSessionId();
        ConstructorInput constructorInput = model.getInput();
        User user = model.getUser();
        process.sendNewConstruction(sessionId, constructorInput, user.getUserId());
    }

    @Override
    public void visit(MessageConstructedUpdate model) {
        String sId = model.getSessionId();
        String mId = model.getMessage().getBody().getMid();
        long senderId = model.getMessage().getSender().getUserId();
        process.publishPoll(sId, mId, senderId);
    }

    @Override
    public void visit(MessageChatCreatedUpdate model) {
        logger.info("I AM in MessageChatCreatedUpdate");
    }

    @Override
    public void visitDefault(Update model) {

    }

}
