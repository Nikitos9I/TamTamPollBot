package com.freegroupdevelopers.TamTamVoteBot.support;

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
 * Date: 30.07.2020
 * (!_!)
 */

@Service
public class Pinger {

    private final Logger logger;

    @Value("${tamtambot.pinger.url}")
    String pingTask;

    public Pinger(BotLogger logger) {
        this.logger = logger.getLogger(Pinger.class);
    }

    @Scheduled(fixedDelayString = "${tamtambot.pinger.period}")
    public void ping() {
        try {
            URL url = new URL(pingTask);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            logger.info("Ping {}, response {}", url.getHost(), connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {
            logger.warn("Error occurred during ping process");
        }
    }
}
