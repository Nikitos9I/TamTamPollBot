package com.freegroupdevelopers.TamTamVoteBot.support.text;

/**
 * Project: TamTamBot
 * This code was created by nikita.savinov
 * Date: 18.07.2020
 * (!_!)
 */

public class TextEN extends Text {

    @Override
    public String helloMessage() {
        return "Hello, this is a VoteBot.\n\nAt first, please, press \"Create a poll\" in bot menu";
    }

    @Override
    public String descriptionMessage() {
        return "For poll creation, please, press \"Create a poll\" in bot menu";
    }

    @Override
    public String questionMessage() {
        return "Enter your question";
    }

    @Override
    public String answerMessage(int num) {
        if (num <= 8) {
            return "Enter option " + num;
        } else {
            return "Your poll was created. Press Send button";
        }
    }

    @Override
    public String globalCount(int amount) {
        if (amount == 0) {
            return "Nobody voted so far";
        } else {
            return amount + " persons voted so far";
        }
    }

    @Override
    public String pollType(boolean isPublic) {
        if (isPublic) {
            return "Public poll: ";
        } else {
            return "Anonymous poll: ";
        }
    }

    @Override
    public String publicPoll() {
        return "Public";
    }

    @Override
    public String anonPoll() {
        return "Anonymous";
    }

    @Override
    public String choosePoll() {
        return "Please, choose type of poll";
    }

    @Override
    public String yourQuestion() {
        return ". Your question is: ";
    }

}
