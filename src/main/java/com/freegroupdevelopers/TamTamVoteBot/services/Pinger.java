package com.freegroupdevelopers.TamTamVoteBot.services;

import com.freegroupdevelopers.TamTamVoteBot.support.BotLogger;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 31.07.2020
 * (!_!)
 */

@Service
public class Pinger {

    private final Logger logger;

    @Value("${tamtambot.pinger.host}")
    String url;

    public Pinger(BotLogger logger) {
        this.logger = logger.getLogger(Pinger.class);
    }

    @Scheduled(fixedDelayString = "${tamtambot.pinger.period}")
    public void ping() {
        try {
            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            logger.info("Ping sended to {}, response {}", this.url, connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {
            logger.warn("error during pinging");
        }
    }

}
