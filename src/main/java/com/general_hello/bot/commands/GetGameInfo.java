package com.general_hello.bot.commands;

import com.general_hello.Bot;
import com.general_hello.bot.ButtonPaginator;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.Game;
import com.general_hello.bot.objects.GlobalVariables;
import com.general_hello.bot.utils.JsonUtils;
import com.general_hello.bot.utils.OddsGetter;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class GetGameInfo extends SlashCommand {
    public GetGameInfo() {
        this.name = "info";
        this.options = Collections.singletonList(
                new OptionData(OptionType.STRING, "name", "The name of the game to get info about").setAutoComplete(true));
        this.cooldown = 100;
        this.help = "Get the info of a game";
        this.guildOnly = false;
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        String closestId = FuzzySearch.extractTop(
                event.getOption("name").getAsString(), OddsGetter.gameNames, 1)
                .get(0).getString();
        event.replyChoice(closestId, closestId).queue();
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        User author = event.getUser();
        if (!DataUtils.hasAccount(author.getIdLong())) {
            event.reply("Kindly make an account first by using `/register`").setEphemeral(true).queue();
            return;
        }

        if (event.isFromGuild()) {
            event.reply("This command is only available in DMs").setEphemeral(true).queue();
            return;
        }

        if (DataUtils.isBanned(author.getIdLong())) {
            event.reply("You are banned from using the bot.").setEphemeral(true).queue();
            return;
        }
        if (event.getOption("name") == null) {
            ArrayList<String> ids = new ArrayList<>();
            for (String id : OddsGetter.gameIdToGame.keySet()) {
                Game game = OddsGetter.gameIdToGame.get(id);
                ids.add("[" + game.getHomeTeam() + "](" + GlobalVariables.LINK + ") **vs** [" +
                                game.getAwayTeam() + "](" + GlobalVariables.LINK + ") " +
                        TimeFormat.RELATIVE.format(game.getGameTime() * 1000) + "\n");
            }

            if (ids.isEmpty()) {
                event.reply("No games found.").setEphemeral(true).queue();
                return;
            }

            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                    .setTitle("Here are the games available for lookup")
                    .setItems(ids)
                    .setItemsPerPage(5)
                    .setColor(Color.WHITE)
                    .setFooter("Do /info <game name> to get the info of a game")
                    .setTimeout(120, TimeUnit.SECONDS)
                    .setEventWaiter(Bot.getBot().getEventWaiter())
                    .addAllowedUsers(author.getIdLong());
            builder.build().paginate(event, 1);
            return;
        }
        String id = event.getOption("name").getAsString();
        if (OddsGetter.gameNames.contains(id)) {
            Game game = null;
            for (Game gameTest : OddsGetter.games) {
                if ((gameTest.getHomeTeam() + " vs " + gameTest.getAwayTeam()).equalsIgnoreCase(id)) {
                    game = gameTest;
                }
            }

            if (game == null) {
                event.reply("Somehow I can't locate the game you want me to output...").setEphemeral(true).queue();
                return;
            }
            Game finalGame = game;
            event.replyEmbeds(OddsGetter.getEmbedPersonal(finalGame).build())
                    .queue((message) -> JsonUtils.incrementInteraction(finalGame.getSportType().getName(), author.getId()), new ErrorHandler()
                            .ignore(ErrorResponse.CANNOT_SEND_TO_USER));

        } else {
            event.reply("Game not found.").setEphemeral(true).queue();
        }
    }
}
