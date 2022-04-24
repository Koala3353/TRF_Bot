package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Item;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.general_hello.commands.Objects.RPGEmojis.berri;

public class ItemInfoCommand extends SlashCommand {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");
    public ItemInfoCommand() {
        this.name = "iteminfo";
        this.help = "To show information " +
                "(description etc) of available item in game";
        this.cooldown = 5;
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "item", "The item name to see the details of").setRequired(true).setAutoComplete(true));
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        if (event.getFocusedOption().getName().equals("item")) {
            List<ExtractedResult> item = FuzzySearch.extractTop(event.getOption("item").getAsString(), Initializer.allNames, 5);
            Collection<Command.Choice> choices = new ArrayList<>();
            int x = 0;
            while (x < item.size()) {
                choices.add(new Command.Choice(item.get(x).getString(), item.get(x).getString()));
                x++;
            }
            event.replyChoices(choices).queue();
        }
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }

        String name = event.getOption("item").getAsString();
        name = FuzzySearch.extractOne(name, Initializer.allNames).getString();

        boolean isErrorOutputed = false;
        try {
            if (Initializer.nameToItem.containsKey(name)) {
                Item item = Initializer.nameToItem.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(item.getName() + " (" + Player.getItemCount(event.getUser().getIdLong(), name) + " owned)");
                embedBuilder.setColor(Color.CYAN);
                embedBuilder.setDescription("> " + item.getDescription() + "\n\n" +
                        "**BUY** - " + (item.getCostToBuy() == null ? "Out of Stock" : berri + " [" + item.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")") + "\n" +
                        "**SELL** - " + berri + " [" + item.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")\n" +
                        "**TRADE** - Unknown");
                embedBuilder.addField("Type", "`Item`", true);
                try {
                    embedBuilder.setThumbnail(item.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
                event.replyEmbeds(embedBuilder.build()).queue();
                return;
            }
        } catch (Exception e) {
            event.reply("The item you searched for is not there!").queue();
            e.printStackTrace();
            isErrorOutputed = true;
        }

        if (!isErrorOutputed) {
            event.reply("The item you searched for is not there!").queue();
        }
    }
}