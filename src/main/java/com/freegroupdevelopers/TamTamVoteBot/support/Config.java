package com.freegroupdevelopers.TamTamVoteBot.support;

import chat.tamtam.botapi.TamTamBotAPI;
import chat.tamtam.botapi.client.TamTamSerializer;
import chat.tamtam.botapi.client.impl.JacksonSerializer;
import com.freegroupdevelopers.TamTamVoteBot.actions.UpdateVisitor;
import lombok.Getter;
import org.slf4j.Logger;
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
    private static String locale = "EN";
    private static Logger logger;

    @Getter
    private static List<String> subscriptions = asList("message_construction_request", "message_created",
            "message_callback", "bot_started", "message_constructed");

    public Config(BotLogger logger) {
        Config.logger = logger.getLogger(UpdateVisitor.class);
    }

    @Bean
    public TamTamBotAPI getApi(@Value("${tamtambot.controller.access_token}") String token) {
        return TamTamBotAPI.create(token);
    }

    @Bean
    public TamTamSerializer getTamTamSerializer() {
        return new JacksonSerializer();
    }

    public static void setLocale(String locale) {
        Config.locale = locale;
        logger.info("Locale switched to " + Config.locale);
    }

}
