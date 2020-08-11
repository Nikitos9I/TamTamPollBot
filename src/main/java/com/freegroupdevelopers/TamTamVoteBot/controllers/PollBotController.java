package com.freegroupdevelopers.TamTamVoteBot.controllers;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.TamTamSerializer;
import chat.tamtam.botapi.exceptions.APIException;
import chat.tamtam.botapi.exceptions.ClientException;
import chat.tamtam.botapi.exceptions.SerializationException;
import chat.tamtam.botapi.model.*;
import com.freegroupdevelopers.TamTamVoteBot.actions.UpdateVisitor;
import com.freegroupdevelopers.TamTamVoteBot.support.BotLogger;
import com.freegroupdevelopers.TamTamVoteBot.support.Config;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashSet;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 20.07.2020
 * (!_!)
 */

@RestController
@RequestMapping
public class PollBotController {

    private static final String ENDPOINT = "subscribe_to_updates";
    private static final String PHOTO_URL = "https://content.foto.my.mail.ru/mail/2213000497/_myphoto/h-1.jpg";

    private final TamTamBotAPI tamTamBotAPI;
    private final Logger logger;
    private final TamTamSerializer serializer;
    private final Update.Visitor handler;

    public PollBotController(TamTamBotAPI tamTamBotAPI, BotLogger logger, TamTamSerializer serializer, UpdateVisitor updateVisitor) {
        this.tamTamBotAPI = tamTamBotAPI;
        this.logger = logger.getLogger(PollBotController.class);
        this.serializer = serializer;
        this.handler = updateVisitor;
    }

    @Value("${tamtambot.controller.host}")
    private String host;

    @PostConstruct
    private void setupBot() throws ClientException, APIException {
        if (tamTamBotAPI == null) {
            return;
        }

        PhotoAttachmentRequestPayload photoAttachment = new PhotoAttachmentRequestPayload()
                .url(PHOTO_URL);

        BotPatch botPatch = new BotPatch()
                .name("PollBot")
                .description("TamTam bot for creating polls. Add poll to any chat.")
                .photo(photoAttachment);

        tamTamBotAPI.editMyInfo(botPatch).execute();

        logger.info("Bot was configured");
    }

    @PostConstruct
    private void subscribe() throws ClientException, APIException {
        if (tamTamBotAPI == null) {
            return;
        }

        final SubscriptionRequestBody body = new SubscriptionRequestBody(host + ENDPOINT)
                .updateTypes(new HashSet<>(Config.getSubscriptions()));

        tamTamBotAPI.subscribe(body).execute();

        logger.info("Subscribed to: " + Config.getSubscriptions());
    }

    @RequestMapping(ENDPOINT)
    @ResponseStatus(value = HttpStatus.OK)
    public void getUpdate(@RequestBody String body) {
        Update update = parseUpdate(body);

        if (update != null) {
            update.visit(handler);
        }
    }

    @RequestMapping("ping")
    @ResponseStatus(value = HttpStatus.OK)
    public void pong() {
        logger.info("Ping caught: pong");
    }

    @Nullable
    protected Update parseUpdate(String response) {
        try {
            return serializer.deserialize(response, Update.class);
        } catch (SerializationException e) {
            logger.error("Error occurred while parsing web hook response " + response);
            return null;
        }
    }

}
