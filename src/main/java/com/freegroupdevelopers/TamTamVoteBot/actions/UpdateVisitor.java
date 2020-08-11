package com.freegroupdevelopers.TamTamVoteBot.actions;

import chat.tamtam.botapi.model.*;
import com.freegroupdevelopers.TamTamVoteBot.services.ServiceImpl;
import com.freegroupdevelopers.TamTamVoteBot.support.BotLogger;
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
        long userId = model.getMessage().getSender().getUserId();

        process.sendDescMessage(userId, model.getUserLocale());
    }

    @Override
    public void visit(MessageCallbackUpdate model) {
        String mId = Objects.requireNonNull(model.getMessage()).getBody().getMid();
        String payload = model.getCallback().getPayload();
        User user = model.getCallback().getUser();
        String callbackId = model.getCallback().getCallbackId();
        process.updatePublishedVote(payload, mId, user, callbackId);
    }

    @Override
    public void visit(MessageEditedUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(MessageRemovedUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(BotAddedToChatUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(BotRemovedFromChatUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(UserAddedToChatUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(UserRemovedFromChatUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(BotStartedUpdate model) {
        User user = model.getUser();
        long userId = user.getUserId();

        service.saveUser(user.getUserId(), user.getUsername());
        process.sayHello(userId, model.getUserLocale());

        logger.info("Bot was added by user: " + user.getUserId());
    }

    @Override
    public void visit(ChatTitleChangedUpdate model) {
        // Not subscribed
    }

    @Override
    public void visit(MessageConstructionRequest model) {
        String sessionId = model.getSessionId();
        ConstructorInput constructorInput = model.getInput();
        User user = model.getUser();
        process.sendNewConstruction(sessionId, constructorInput, user.getUserId(), model.getUserLocale());
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
        // Not subscribed
    }

    @Override
    public void visitDefault(Update model) {
        logger.warn("Visit Default");
    }

}
