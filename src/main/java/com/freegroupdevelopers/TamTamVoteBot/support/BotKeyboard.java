package com.freegroupdevelopers.TamTamVoteBot.support;

import chat.tamtam.botapi.model.*;
import com.freegroupdevelopers.TamTamVoteBot.support.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: TamTamVoteBot
 * This code was created by nikita.savinov
 * Date: 26.07.2020
 * (!_!)
 */

public class BotKeyboard {

    public static Keyboard getInitialKeyboard(String locale) {
        List<List<Button>> buttons = new ArrayList<>();
        buttons.add(new ArrayList<>());

        Button pb = new CallbackButton("public", Text.text(locale).publicPoll());
        Button ab = new CallbackButton("anon", Text.text(locale).anonPoll());

        buttons.get(0).add(pb);
        buttons.get(0).add(ab);

        return new Keyboard(buttons);
    }

}
