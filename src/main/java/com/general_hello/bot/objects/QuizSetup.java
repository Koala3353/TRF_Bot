package com.general_hello.bot.objects;

import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.database.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.*;

public class QuizSetup {
    public static HashMap<Long, QuizSetup> userIdToQuizSetupHashMap = new HashMap<>();
    public static final String SEPARATOR_OPTION = "%&";
    public static final String QUESTION = "Question";
    public static final String ROLE_ID = "RoleId";
    public static final String REQUIRED_ROLE_ID = "ReqRoleId";
    public static final String OPTIONS = "Options";
    public static final String ANSWERS = "CorrectAnswer";
    private final long channelId;
    private long rewardRole;
    private long requiredRole;
    private final LinkedList<String> questions = new LinkedList<>();
    private final HashMap<String, ArrayList<String>> optionsPerQuestion = new HashMap<>();
    private final HashMap<String, String> questionToCorrectAnswer = new HashMap<>();

    public QuizSetup(long userid, long channelId) {
        this.channelId = channelId;
        userIdToQuizSetupHashMap.put(userid, this);
    }

    public long getChannelId() {
        return channelId;
    }

    public static QuizSetup getQuizSetup(long userid) {
        return userIdToQuizSetupHashMap.get(userid);
    }

    public void save(long userid) {
        userIdToQuizSetupHashMap.put(userid, this);
    }

    public synchronized boolean finish() {
        boolean error = false;
        // Make a new Quiz table if it doesn't exist
        try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                .prepareStatement("CREATE TABLE IF NOT EXISTS Quiz" + getChannelId() + " ( " +
                        "RoleId INTEGER NOT NULL," +
                        "Question TEXT NOT NULL," +
                        "Options TEXT NOT NULL," +
                        "CorrectAnswer TEXT NOT NULL," +
                        "QuestionNumber INTEGER NOT NULL," +
                        "ReqRoleId INTEGER NOT NULL" +
                        ");")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            error = true;
        }

        if (!error) {
            LinkedList<String> questionsList = getQuestions();
            HashMap<String, ArrayList<String>> optionsOfQuestion = getOptionsPerQuestion();
            HashMap<String, String> answers = getQuestionToCorrectAnswer();
            StringBuilder options = new StringBuilder();

            for (int i = 0; i < questionsList.size(); i++) {
                try (final PreparedStatement preparedStatement = SQLiteDataSource.getConnection()
                        .prepareStatement("INSERT INTO Quiz" + getChannelId() +
                                "(RoleId, Question, Options, CorrectAnswer, QuestionNumber, ReqRoleId)" +
                                "VALUES (?, ?, ?, ?, ?, ?);")) {
                    String question = questionsList.get(i);
                    StringBuilder finalOptions = options;
                    optionsOfQuestion.get(question).forEach(option -> finalOptions.append(option).append(SEPARATOR_OPTION));
                    String[] split = SEPARATOR_OPTION.split("");
                    options.deleteCharAt(options.lastIndexOf(split[0]));
                    options.deleteCharAt(options.lastIndexOf(split[1]));
                    preparedStatement.setString(1, String.valueOf(getRewardRole()));
                    preparedStatement.setString(2, question);
                    preparedStatement.setString(3, options.toString());
                    preparedStatement.setString(4, answers.get(question));
                    preparedStatement.setInt(5, (i+1));
                    preparedStatement.setLong(6, getRequiredRole());

                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    error = true;
                }

                options = new StringBuilder();
            }
        }

        return error;
    }

    public long getRequiredRole() {
        return requiredRole;
    }

    public QuizSetup newQuestion(String question, String correctAnswer, List<String> options) {
        questions.add(question);
        optionsPerQuestion.put(question, new ArrayList<>(options));
        questionToCorrectAnswer.put(question, correctAnswer);
        return this;
    }

    public int getQuestionCount() {
        return questions.size();
    }

    public void delete(long userid) {
        userIdToQuizSetupHashMap.remove(userid);
    }

    public QuizSetup setRequiredRole(long requiredRole) {
        this.requiredRole = requiredRole;
        return this;
    }

    public long getRewardRole() {
        return rewardRole;
    }

    public QuizSetup setRewardRole(long rewardRole) {
        this.rewardRole = rewardRole;
        return this;
    }

    public LinkedList<String> getQuestions() {
        return questions;
    }


    public HashMap<String, ArrayList<String>> getOptionsPerQuestion() {
        return optionsPerQuestion;
    }

    public HashMap<String, String> getQuestionToCorrectAnswer() {
        return questionToCorrectAnswer;
    }

    public static EmbedBuilder buildEmbed(int question, long channelId) {
        String questionOfQuiz = DataUtils.getQuizQuestionContent(channelId, QUESTION, question);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Question number " + question);
        embedBuilder.setColor(Color.CYAN);
        embedBuilder.setDescription(questionOfQuiz.replaceAll("\n", "\n"));
        embedBuilder.setFooter("Select your answer from the selection menu below.").setTimestamp(OffsetDateTime.now(ZoneId.of("UTC-6")));
        return embedBuilder;
    }

    public static synchronized ActionRow buildActionRow(int questionNumber, long channelId) {
        String optionsOfQuestion = DataUtils.getQuizQuestionContent(channelId, OPTIONS, questionNumber);
        String[] options = optionsOfQuestion.split(SEPARATOR_OPTION);

        // shuffle options
        List<String> optionsList = Arrays.asList(options);
        Collections.shuffle(optionsList);

        StringSelectMenu.Builder selectMenu = StringSelectMenu.create("0000:quizoptions:" + channelId + ":" + questionNumber);
        for (String option : optionsList) {
            selectMenu.addOption(option, option);
        }

        return ActionRow.of(selectMenu.build());
    }
}
