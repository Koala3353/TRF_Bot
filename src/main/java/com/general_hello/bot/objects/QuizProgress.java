package com.general_hello.bot.objects;

import java.util.HashMap;
import java.util.LinkedList;

public class QuizProgress {
    public static HashMap<Long, QuizProgress> userToQuizProgressHashMap = new HashMap<>();

    private final long channelId;
    private final LinkedList<String> answers;
    private final long userId;

    public QuizProgress(long channelId, String answer, long userId) {
        this.channelId = channelId;
        this.answers = new LinkedList<>();
        this.answers.add(answer);
        this.userId = userId;
        userToQuizProgressHashMap.put(userId, this);
    }

    public QuizProgress addAnswer(String answer) {
        answers.add(answer);
        return this;
    }

    public static QuizProgress getQuizProgress(long userId) {
        return userToQuizProgressHashMap.get(userId);
    }

    public QuizProgress save(long userId) {
        return userToQuizProgressHashMap.put(userId, this);
    }

    public QuizProgress remove(long userId) {
        return userToQuizProgressHashMap.remove(userId);
    }

    public long getChannelId() {
        return channelId;
    }

    public LinkedList<String> getAnswers() {
        return answers;
    }

    public long getUserId() {
        return userId;
    }
}
