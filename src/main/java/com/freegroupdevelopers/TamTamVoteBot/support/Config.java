package com.freegroupdevelopers.TamTamVoteBot.support;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.TamTamSerializer;
import chat.tamtam.botapi.client.impl.JacksonSerializer;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 20.07.2020
 * (!_!)
 */

@Configuration
public class Config {

    @Getter
    private static List<String> subscriptions = asList("message_construction_request", "message_created",
            "message_callback", "bot_started", "message_constructed");

    @Bean
    public TamTamBotAPI getApi(@Value("${tamtambot.controller.access_token}") String token) {
        return TamTamBotAPI.create(token);
    }

    @Bean
    public TamTamSerializer getTamTamSerializer() {
        return new JacksonSerializer();
    }

}
