package com.freegroupdevelopers.TamTamVoteBot.support.text;

import com.freegroupdevelopers.TamTamVoteBot.support.Config;

/**
 * Project: TamTamBot
 * This code was created by nikita.savinov
 * Date: 18.07.2020
 * (!_!)
 */

public abstract class Text {

    public static Text text(String locale) {
        switch (locale) {
            case "ru":
                return new TextRU();
            default:
                return new TextEN();
        }
    }

    public static String vote = "✔️";
    public static String empty = "◻️";
    public static String zero = "0️⃣";
    public static String one = "1️⃣";
    public static String two = "2️⃣";
    public static String three = "3️⃣";
    public static String four = "4️⃣";
    public static String five = "5️⃣";
    public static String six = "6️⃣";
    public static String seven = "7️⃣";
    public static String eight = "8️⃣";

    public static String getPrefix(int num) {
        switch (num) {
            case 0:
                return zero;
            case 1:
                return one;
            case 2:
                return two;
            case 3:
                return three;
            case 4:
                return four;
            case 5:
                return five;
            case 6:
                return six;
            case 7:
                return seven;
            case 8:
                return eight;
            default:
                return vote;
        }
    }

    public abstract String helloMessage();
    public abstract String descriptionMessage();
    public abstract String questionMessage();
    public abstract String answerMessage(int num);
    public abstract String globalCount(int amount);
    public abstract String pollType(boolean isPublic);
    public abstract String publicPoll();
    public abstract String anonPoll();
    public abstract String choosePoll();
    public abstract String yourQuestion();
}
