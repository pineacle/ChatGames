package me.pineacle.chatgames.game;

import me.pineacle.chatgames.user.User;

import java.util.Map;

public abstract class Question {

    protected String question;
    protected int timeLimit;
    private Map<User, Integer> records;

}
