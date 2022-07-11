package com.general_hello.commands.events;

import com.general_hello.commands.commands.DashboardCommand;
import com.general_hello.commands.database.DataUtils;
import com.general_hello.commands.objects.GlobalVariables;
import com.general_hello.commands.objects.SpecialPost;
import com.general_hello.commands.utils.JsonUtils;
import com.general_hello.commands.utils.OddsGetter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

public class OnButtonClick extends ListenerAdapter {
    private static Logger LOGGER = LoggerFactory.getLogger(OnButtonClick.class);
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        // users can spoof this id so be careful what you do with this
        String[] id = event.getComponentId().split(":"); // this is the custom id we specified in our button
        String authorId = id[0];

        if (id.length == 1) {
            return;
        }

        String type = id[1];

        // When storing state like this is it is highly recommended doing some kind of verification that it was generated by you, for instance a signature or local cache
        if (!authorId.equals("0000") && !authorId.equals(event.getUser().getId())) {
            event.reply("You can't press this button").setEphemeral(true).queue();
            return;
        }

        User author = event.getUser();
        if (!DataUtils.hasAccount(author.getIdLong())) {
            event.reply("Kindly make an account first by using `/register`").setEphemeral(true).queue();
            return;
        }

        if (DataUtils.isBanned(author.getIdLong())) {
            event.reply("You are banned from using the bot.").setEphemeral(true).queue();
            return;
        }

        switch (type) {
            case "bet" -> {
                String teamName = id[2];
                String gameId = id[3];
                if (DataUtils.newBet(event.getUser().getIdLong(), teamName, gameId)) {
                    JsonUtils.incrementInteraction(OddsGetter.gameIdToGame.get(gameId).getSportType().getName(), author.getId());
                    event.reply("You have placed a bet on " + teamName + " in game " + gameId).setEphemeral(true).queue();
                } else {
                    event.reply("You have already placed a bet on " + teamName + " in game " + gameId).setEphemeral(true).queue();
                }
            }
        }
    }

    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        String value = event.getSelectedOptions().get(0).getValue();

        switch (value) {
            case "setresult":
                DashboardCommand.LAST_USED.setSetResult(Instant.now().getEpochSecond() * 1000);
                DashboardCommand.buildAndSend(event.getTextChannel());
                TextInput gameID = TextInput.create("gameid", "Game ID", TextInputStyle.SHORT)
                        .setPlaceholder("The ID of the game")
                        .setMinLength(30)
                        .setMaxLength(35)
                        .build();

                TextInput body = TextInput.create("teamname", "Winner", TextInputStyle.SHORT)
                        .setPlaceholder("The name of the winner's team")
                        .setMinLength(5)
                        .setMaxLength(100)
                        .build();

                Modal modal = Modal.create("setresult", "Set Result")
                        .addActionRows(ActionRow.of(gameID), ActionRow.of(body))
                        .build();

                event.replyModal(modal).queue();
                break;
            case "shutdown":
                DashboardCommand.LAST_USED.setShutdown(Instant.now().getEpochSecond() * 1000);
                DashboardCommand.buildAndSend(event.getTextChannel());
                event.reply("Shutting down... Goodbye!").setEphemeral(true).queue();
                event.getJDA().shutdown();
                LOGGER.info("Bot shut down by " + event.getUser().getAsTag());
                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.exit(0);
                break;
            case "reloaddata":
                try {
                    DashboardCommand.LAST_USED.setReloadData(Instant.now().getEpochSecond() * 1000);
                    DashboardCommand.buildAndSend(event.getTextChannel());
                    event.reply("Reloading the data... Check the logs for more details").setEphemeral(true).queue();
                    new OddsGetter.GetOddsTask().run();
                }  catch (Exception ignored) {}
                break;
            case "hof":
                DashboardCommand.LAST_USED.setHof(Instant.now().getEpochSecond() * 1000);
                List<SpecialPost> specialPosts = DataUtils.getSpecialPosts();
                Collections.sort(specialPosts);
                if (specialPosts.isEmpty()) {
                    event.reply("No special posts yet").setEphemeral(true).queue();
                    return;
                }
                SpecialPost topPost = specialPosts.get(0);
                DataUtils.setTopPost(topPost.getUnixTime());
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Color.orange);
                String posterId = DataUtils.getPoster(topPost.getUnixTime());
                User userById = event.getJDA().getUserById(posterId);
                embedBuilder.setAuthor(userById.getName(), null, userById.getEffectiveAvatarUrl());
                embedBuilder.setDescription(DataUtils.getContent(topPost.getUnixTime()));
                event.getGuild().getTextChannelById(GlobalVariables.HALL_OF_FAME).sendMessageEmbeds(embedBuilder.build()).queue();
                break;
            case "extractdata":
                DashboardCommand.LAST_USED.setExtractData(Instant.now().getEpochSecond() * 1000);
                JSONArray finalArray = new JSONArray();
                List<Long> champs = DataUtils.getChamps();
                if (champs != null) {
                    for (long champ : champs) {
                        User champUser = event.getJDA().getUserById(champ);
                        JSONObject champObject = new JSONObject();
                        champObject.put("champName", champUser.getAsTag());
                        champObject.put("userId", champ);
                        champObject.put("champWallet", DataUtils.getSolanaAddress(champ));
                        champObject.put("hofCount", DataUtils.getHofCount(champ));
                        champObject.put("highInteractionPosts", DataUtils.getHighInteractionPosts(champ));
                        champObject.put("followerCount", DataUtils.getFollowersOfChamp(champ).size());
                        finalArray.put(champObject);
                    }

                    //Write JSON file
                    long time = System.currentTimeMillis();
                    try (FileWriter file = new FileWriter("champs-" + time + ".json")) {
                        //We can write any JSONArray or JSONObject instance to the file
                        file.write(finalArray.toString());
                        file.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File file = new File("champs-" + time + ".json");
                    event.replyFile(file, "champs-" + time + ".json").setEphemeral(true).queue();
                    LOGGER.info("Made a new champs-" + time + ".json file");
                } else {
                    LOGGER.info("No champs found");
                    event.reply("No champs found").setEphemeral(true).queue();
                }
                break;
        }
    }
}
