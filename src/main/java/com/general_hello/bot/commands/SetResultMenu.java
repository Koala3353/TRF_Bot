package com.general_hello.bot.commands;

import com.general_hello.bot.objects.Game;
import com.general_hello.bot.utils.OddsGetter;
import com.jagrosh.jdautilities.command.MessageContextMenu;
import com.jagrosh.jdautilities.command.MessageContextMenuEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

public class SetResultMenu extends MessageContextMenu {
    public SetResultMenu() {
        this.name = "Set Result";
        this.userPermissions = new Permission[]{
                Permission.MANAGE_SERVER,
                Permission.MESSAGE_MANAGE
        };
    }

    @Override
    protected void execute(MessageContextMenuEvent event) {
        Game game = null;
        for (String gameId : OddsGetter.gameIdToMessageId.keySet()) {
            Long messageId = OddsGetter.gameIdToMessageId.get(gameId);
            if (event.getTarget().getIdLong() == messageId) {
                game = OddsGetter.gameIdToGame.get(gameId);
            }
        }

        if (game == null) {
            event.reply("Unknown game.").setEphemeral(true).queue();
            return;
        }

        SelectMenu menu = SelectMenu.create("menu:winning")
                .setPlaceholder("Choose the winning team") // shows the placeholder indicating what this menu is for
                .setRequiredRange(1, 1) // only one can be selected
                .addOption(game.getHomeTeam(), game.getId() + ":" + game.getHomeTeam())
                .addOption(game.getAwayTeam(), game.getId() + ":" + game.getAwayTeam())
                .build();

        event.reply("Please select the winning team below")
                .setEphemeral(true)
                .addActionRow(menu)
                .queue();
    }
}
