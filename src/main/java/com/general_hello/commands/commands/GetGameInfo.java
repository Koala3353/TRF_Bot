package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.database.DataUtils;
import com.general_hello.commands.objects.Game;
import com.general_hello.commands.utils.JsonUtils;
import com.general_hello.commands.utils.OddsGetter;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class GetGameInfo extends SlashCommand {
    public GetGameInfo() {
        this.name = "info";
        this.options = Collections.singletonList(
                new OptionData(OptionType.STRING, "id", "The id of the game to get info about").setAutoComplete(true));
        this.cooldown = 100;
        this.help = "Get the info of a game";
        this.guildOnly = false;
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        String closestId = FuzzySearch.extractTop(
                event.getOption("id").getAsString(), OddsGetter.gameIdToGame.keySet(), 1)
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

        if (DataUtils.isBanned(author.getIdLong())) {
            event.reply("You are banned from using the bot.").setEphemeral(true).queue();
            return;
        }
        if (event.getOption("id") == null) {
            ArrayList<String> ids = new ArrayList<>();
            for (String id : OddsGetter.gameIdToGame.keySet()) {
                Game game = OddsGetter.gameIdToGame.get(id);
                ids.add("**" + id + "** - " + game.getHomeTeam() + " vs " +
                                game.getAwayTeam() + " " +
                        TimeFormat.RELATIVE.format(game.getGameTime() * 1000) + "\n");
            }

            if (ids.isEmpty()) {
                event.reply("No games found.").setEphemeral(true).queue();
                return;
            }

            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                    .setColor(event.getGuild().getSelfMember().getColor())
                    .setTitle("Here are the games available for lookup")
                    .setItems(ids)
                    .setItemsPerPage(5)
                    .setFooter("Do /info <id> to get the info of a game")
                    .setTimeout(120, TimeUnit.SECONDS)
                    .setEventWaiter(Bot.getBot().getEventWaiter())
                    .addAllowedUsers(author.getIdLong());
            builder.build().paginate(event, 1);
            return;
        }
        String id = event.getOption("id").getAsString();
        if (OddsGetter.gameIdToGame.containsKey(id)) {
            Game game = OddsGetter.gameIdToGame.get(id);
            author.openPrivateChannel().flatMap(channel ->
                    channel.sendMessageEmbeds(OddsGetter.getEmbedPersonal(game).build()))
                    .queue((message) -> {
                        event.reply("Check your DMs!").setEphemeral(true).queue();
                        JsonUtils.incrementInteraction(game.getSportType().getName(), author.getId());
                    }, new ErrorHandler()
                            .handle(
                                    ErrorResponse.CANNOT_SEND_TO_USER,
                                    (e) -> event.reply("I couldn't send a private message to you.")
                                            .setEphemeral(true).queue()));

        } else {
            event.reply("Game not found.").setEphemeral(true).queue();
        }
    }
}
