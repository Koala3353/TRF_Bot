package com.general_hello.commands.commands.stage2;

import com.general_hello.Config;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.Trade.Offer;
import com.general_hello.commands.Objects.Emojis.RPGEmojis;
import com.general_hello.commands.Objects.Trade.Trade;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;

import java.util.Collections;

public class TradeCommand extends SlashCommand {
    public TradeCommand() {
        this.name = "trade";
        this.help = "Trade items with another user";
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "user", "The user you want to trade with").setRequired(true));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (!event.getGuild().getId().equals(Config.get("guild"))) {
            event.reply("This command can only be used in the main discord server.").queue();
            return;
        }

        if (!event.getTextChannel().getId().equals(Config.get("trade")) && !event.getTextChannel().getId().equals(Config.get("tradepaid"))) {
            event.reply("Go to " + event.getGuild().getTextChannelById(Config.get("trade")).getAsMention() + " to trade.").queue();
            return;
        }

        // Checks if they made an account, has an ongoing trade, mentioned himself, a user that didn't made an account or a bot
        if (DataUtils.makeCheck(event)) {
            return;
        }

        User target = event.getOption("user").getAsUser();
        User proposer = event.getUser();

        if (Trade.loadTrade(proposer.getIdLong()) != null) {
            event.reply("You already have an ongoing trade!").queue();
            return;
        }
        if (target.getId().equals(proposer.getId())) {
            event.reply("You can't trade with yourself").queue();
            return;
        }

        if (target.isBot()) {
            event.reply("You can't trade with bots").queue();
            return;
        }

        if (DataUtils.hasAccount(target) == -1) {
            event.reply(target.getAsMention() + " didn't made an account still.").setEphemeral(true).queue();
            return;
        }

        // Builds the embed and makes a new trade
        EmbedBuilder embedBuilder = new Trade(proposer.getIdLong(), target.getIdLong(), new Offer(), new Offer())
                .save().buildEmbed();

        // Makes a selection menu
        SelectMenu menu = SelectMenu.create("trade:" + proposer.getId())
                .setPlaceholder("Choose the option")
                .setRequiredRange(1, 1)
                .addOption("Add Items", "additems", "Adds items for the trade", Emoji.fromMarkdown("<:egyptian_broken_weapon:905969748582469682>"))
                .addOption("Change Items", "changeitems", "Resets your offer of items and adds items", Emoji.fromMarkdown("<a:loading:870870083285712896>"))
                .addOption("Add Berri", "addberri", "Resets the berri and place your offer for it", Emoji.fromMarkdown(RPGEmojis.berri))
                .addOption("Add Rainbow Shards", "addrainbowshards", "Resets the rainbow shards and place your offer for it", Emoji.fromMarkdown(RPGEmojis.rainbowShards))
                .build();
        // Replies with an embed with buttons
        event.replyEmbeds(embedBuilder.build())
                .addActionRows(
                        ActionRow.of(menu),
                        ActionRow.of(
                                Button.secondary(proposer.getId() + ":switch", "Switch Editing Offer").withEmoji(Emoji.fromMarkdown("<:right:915425310592356382>")),
                                Button.danger(proposer.getId() + ":reset", "Reset Trade").withEmoji(Emoji.fromMarkdown("<:xmark:957420842898292777>")),
                                Button.success(proposer.getId() + ":submit", "Submit Trade").withEmoji(Emoji.fromMarkdown("<:check:957420541256531969>")),
                                Button.danger(proposer.getId() + ":cancel", "Cancel Trade").withEmoji(Emoji.fromMarkdown("<:xmark:957420842898292777>"))
                                )
                        ).queue();
    }
}
