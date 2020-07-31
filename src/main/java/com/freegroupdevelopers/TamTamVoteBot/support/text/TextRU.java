package com.freegroupdevelopers.TamTamVoteBot.support.text;

/**
 * Project: TamTamBot
 * This code was created by nikita.savinov
 * Date: 18.07.2020
 * (!_!)
 */

public class TextRU extends Text {

    @Override
    public String helloMessage() {
        return "Привет! Я PollBot. Я буду помогать Вам в создании простых опросов.\n\nЧтобы создать опрос, нажмите \"Create a poll\" в меню бота";
    }

    @Override
    public String descriptionMessage() {
        return "Для создания опроса нажмите \"Create a poll\" в меню бота";
    }

    @Override
    public String questionMessage() {
        return "Введите ваш вопрос";
    }

    @Override
    public String answerMessage(int num) {
        if (num <= 8) {
            return "Введите " + num + " вариант ответа";
        } else {
            return "Ваш опрос был создан. Нажмите \"Отправить\"";
        }
    }

    @Override
    public String globalCount(int amount) {
        if (amount == 0) {
            return "Пока никто не проголосовал";
        } else {
            return amount + " проголосовало на данный момент";
        }
    }

    @Override
    public String pollType(boolean isPublic) {
        if (isPublic) {
            return "Публичный опрос: ";
        } else {
            return "Анонимный опрос: ";
        }
    }

    @Override
    public String publicPoll() {
        return "Публичный";
    }

    @Override
    public String anonPoll() {
        return "Анонимный";
    }

    @Override
    public String choosePoll() {
        return "Выберите тип опроса";
    }

    @Override
    public String yourQuestion() {
        return ". Ваш вопрос: ";
    }

}
