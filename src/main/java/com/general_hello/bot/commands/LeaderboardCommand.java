package com.general_hello.bot.commands;

import com.general_hello.Bot;
import com.general_hello.bot.ButtonPaginator;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.GlobalVariables;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LeaderboardCommand extends SlashCommand {
    public static HashMap<Long, Integer> wins = new HashMap<>();
    public static HashMap<Long, Integer> loss = new HashMap<>();

    public LeaderboardCommand() {
        this.name = "leaderboard";
        this.help = "Shows the leaderboard";
        this.userPermissions = new Permission[]{
                Permission.MANAGE_SERVER,
                Permission.MESSAGE_MANAGE
        };
        OptionData data = new OptionData(OptionType.STRING, "leaderboard-type",
                "The type of leaderboard to send")
                .addChoices(new Command.Choice("Predictions leaderboard (All-time)", "plb-all"),
                        new Command.Choice("Predictions leaderboard (Daily)", "plb-daily"),
                        new Command.Choice("Champs leaderboard (Not implemented)", "champ")).setRequired(true);
        this.options = Collections.singletonList(data);
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        OptionMapping leaderboardType = event.getOption("leaderboard-type");
        if (leaderboardType.getAsString().equals("plb-all")) {
            List<Long> userIds = DataUtils.getPredictionUsers();
            if (userIds == null) {
                event.reply("No users have predicted yet!").setEphemeral(true).queue();
                return;
            } else {
                event.reply("Loading the leaderboard...").setEphemeral(true).queue();
            }

            ArrayList<String> pages = new ArrayList<>();
            List<Predictor> predictors = new ArrayList<>();
            for (long userId : userIds) {
                int winCount = DataUtils.getWinCount(userId);
                int loseCount = DataUtils.getLoseCount(userId);
                predictors.add(new Predictor(event.getJDA().getUserById(userId).getAsTag(), winCount, loseCount, userId));
            }

            // Sorting the predictors by win count
            Collections.sort(predictors);

            for (Predictor predictor : predictors) {
                pages.add(predictor.toString());
            }

            if (pages.isEmpty()) {
                return;
            }
            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA()) {
                @Override
                protected ArrayList<String> refresh(ButtonInteractionEvent event) {
                    ArrayList<String> pages = new ArrayList<>();
                    List<Predictor> predictors = new ArrayList<>();
                    for (long userId : userIds) {
                        int winCount = DataUtils.getWinCount(userId);
                        int loseCount = DataUtils.getLoseCount(userId);
                        predictors.add(new Predictor(event.getJDA().getUserById(userId).getAsTag(), winCount, loseCount, userId));
                    }

                    // Sorting the predictors by win count
                    Collections.sort(predictors);

                    for (Predictor predictor : predictors) {
                        pages.add(predictor.toString());
                    }

                    if (pages.isEmpty()) {
                        pages.add("No users have predicted yet!");
                    }
                    return pages;
                }
            };
            builder.setColor(event.getGuild().getSelfMember().getColor());
            builder.setItemsPerPage(7);
            builder.setFooter("Press the buttons below to navigate between the leaderboard");
            builder.setEventWaiter(Bot.getBot().getEventWaiter());
            builder.setTitle("Prediction Leaderboard - All time");
            builder.setTimeout(365, TimeUnit.DAYS);
            builder.setItems(pages);
            builder.build().paginate(event.getChannel().asTextChannel(), 1);
        } else if (leaderboardType.getAsString().equals("plb-daily")) {
            Set<Long> userIds = wins.keySet();
            Set<Long> test = loss.keySet();
            if (userIds.isEmpty() && test.isEmpty()) {
                event.reply("No users have predicted yet!").setEphemeral(true).queue();
                return;
            } else {
                event.reply("Loading the leaderboard...").setEphemeral(true).queue();
            }

            if (userIds.isEmpty()) {
                userIds = test;
            }

            ArrayList<String> pages = new ArrayList<>();
            List<Predictor> predictors = new ArrayList<>();
            for (long userId : userIds) {
                int winCount = wins.getOrDefault(userId, 0);
                int loseCount = loss.getOrDefault(userId, 0);
                predictors.add(new Predictor(event.getJDA().getUserById(userId).getAsTag(), winCount, loseCount, userId));
            }

            // Sorting the predictors by win count
            Collections.sort(predictors);

            for (Predictor predictor : predictors) {
                pages.add(predictor.toString());
            }

            if (pages.isEmpty()) {
                return;
            }
            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA()) {
                @Override
                protected ArrayList<String> refresh(ButtonInteractionEvent event) {
                    Set<Long> userIds = wins.keySet();

                    ArrayList<String> pages = new ArrayList<>();
                    List<Predictor> predictors = new ArrayList<>();
                    for (long userId : userIds) {
                        int winCount = wins.getOrDefault(userId, 0);
                        int loseCount = loss.getOrDefault(userId, 0);
                        predictors.add(new Predictor(event.getJDA().getUserById(userId).getAsTag(), winCount, loseCount, userId));
                    }

                    // Sorting the predictors by win count
                    Collections.sort(predictors);

                    for (Predictor predictor : predictors) {
                        pages.add(predictor.toString());
                    }

                    if (pages.isEmpty()) {
                        pages.add("No users have predicted yet!");
                    }
                    return pages;
                }
            };
            builder.setColor(event.getGuild().getSelfMember().getColor());
            builder.setItemsPerPage(7);
            builder.setFooter("Press the buttons below to navigate between the leaderboard");
            builder.setEventWaiter(Bot.getBot().getEventWaiter());
            builder.setTitle("Prediction Leaderboard - Daily");
            builder.setTimeout(365, TimeUnit.DAYS);
            builder.setItems(pages);
            builder.build().paginate(event.getChannel().asTextChannel(), 1);
        } else if (leaderboardType.getAsString().equals("champ")) {
            event.reply("This feature is not implemented yet!").setEphemeral(true).queue();
        } else {
            event.reply("Invalid leaderboard type!").setEphemeral(true).queue();
        }
    }

    private record Predictor(String userName, int wins, int loss, long userId) implements Comparable<Predictor> {

        public int getWins() {
            return wins;
        }

        public int getLoss() {
            return loss;
        }

        @Override
        public int compareTo(@NotNull LeaderboardCommand.Predictor o) {
            int scoreA = this.wins - this.loss;
            int scoreB = o.getWins() - o.getLoss();

            if (scoreA > scoreB) {
                return -1;
            } else if (scoreA < scoreB) {
                return 1;
            } else {
                return Integer.compare(o.wins, this.wins);
            }
        }

        @Override
        public String toString() {
            return "**" + userName + "** - " +
                    wins + " **W** " + loss + " **L** " +
                    DataUtils.getLastResults(userId).replaceAll("W", GlobalVariables.WIN_EMOJI)
                            .replaceAll("L", GlobalVariables.LOSE_EMOJI).replaceAll("X", "");
        }
    }
}
