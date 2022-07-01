package com.general_hello.commands.events;

import com.general_hello.Bot;
import com.general_hello.commands.database.DataUtils;
import com.general_hello.commands.objects.Game;
import com.general_hello.commands.utils.JsonUtils;
import com.general_hello.commands.utils.OddsGetter;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class OnModalEvent extends ListenerAdapter {
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("register")) {
            String solanaAddress = event.getValue("name").getAsString().replaceAll("\n", "");
            if (DataUtils.verifyAddress(solanaAddress)) {
                DataUtils.newAccount(event.getUser().getIdLong(), solanaAddress);
                event.reply("Successfully stored your Solana Address within the bot.").setEphemeral(true).queue();
            } else {
                event.reply("Invalid Solana Address placed.").setEphemeral(true).queue();
            }
        } else if (event.getModalId().equals("setresult")) {
            String teamName = event.getValue("teamname").getAsString();
            String gameId = event.getValue("gameid").getAsString();
            if (!OddsGetter.gameIdToGame.containsKey(gameId)) {
                event.reply("Invalid game id.").setEphemeral(true).queue();
                return;
            }
            Game game = OddsGetter.gameIdToGame.get(gameId);
            List<String> teams = Arrays.asList(game.getHomeTeam(), game.getAwayTeam());
            teamName = FuzzySearch.extractAll(teamName, teams).get(0).getString();
            String finalTeamName = teamName;
            DataUtils.getUsers(gameId).forEach(userID -> {
                String betTeam = DataUtils.getBetTeam(userID, gameId);
                if (betTeam.equals(finalTeamName)) {
                    JsonUtils.incrementCorrectPred(game.getSportType().getName(), userID);
                } else {
                    JsonUtils.incrementWrongPred(game.getSportType().getName(), userID);
                }
            });

            EmbedBuilder embed = OddsGetter.getEmbedPersonal(game);
            teams.remove(finalTeamName);
            long textChannelId = game.getSportType().getChannelId();
            MessageChannel channel = Bot.getJda().getTextChannelById(textChannelId);
            long messageId = OddsGetter.gameIdToMessageId.get(gameId);
            channel.retrieveMessageById(messageId).queue((message) -> {
                message.editMessageEmbeds(embed.build())
                        .setActionRow(Button.success("winner", finalTeamName).asDisabled(),
                                Button.danger("loser", teams.get(0)).asDisabled()).queue();
            });

            event.reply("Successfully set the result of the game.").setEphemeral(true).queue();
        }
    }
}
