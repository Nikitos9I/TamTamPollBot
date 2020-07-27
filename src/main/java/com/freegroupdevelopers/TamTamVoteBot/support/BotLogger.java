package com.freegroupdevelopers.TamTamVoteBot.support;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 20.07.2020
 * (!_!)
 */

@Component
public class BotLogger {

    public Logger getLogger(Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        BasicConfigurator.configure();
        return logger;
    }

}
