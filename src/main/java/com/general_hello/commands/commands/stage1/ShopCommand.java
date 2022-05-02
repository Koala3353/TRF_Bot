package com.general_hello.commands.commands.stage1;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Items.Item;
import com.general_hello.commands.Objects.Items.LimitedItems;
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
import java.util.concurrent.TimeUnit;

import static com.general_hello.commands.Objects.Emojis.RPGEmojis.berri;

public class ShopCommand extends SlashCommand {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");
    public ShopCommand() {
        this.name = "shop";
        this.help = "Shows the shop";
        this.cooldown = 5;
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "item", "The item name to see the details of").setAutoComplete(true));
    }

    @Override
    public void onAutoComplete(CommandAutoCompleteInteractionEvent event) {
        // Auto complete
        if (event.getFocusedOption().getName().equals("item")) {
            List<ExtractedResult> item = FuzzySearch.extractTop(event.getOption("item").getAsString(), Initializer.nameToItem.keySet(), 2);
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
        // Checks if the user made an account or not
        if (DataUtils.makeCheck(event)) {
            return;
        }

        // Checks if they placed an item or not
        if (event.getOption("item") == null) {
            // Sends the entire shop since they didn't placed an item (null)
            ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                    .setEventWaiter(Bot.getBot().getEventWaiter())
                    .setItemsPerPage(5)
                    .setTimeout(1, TimeUnit.MINUTES)
                    .useNumberedItems(false);

            builder.setTitle("Shop")
                    .setItems(buildDescription(event.getUser().getIdLong()))
                    .addAllowedUsers(event.getUser().getIdLong())
                    .setColor(0x0099E1);
            event.reply("Loading shop...").queue((interactionHook -> {
                interactionHook.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
            }));
            builder.build().paginate(event.getTextChannel(), 1);
            return;
        }
        // Matches the closest item based on the name
        String name = event.getOption("item").getAsString();
        name = FuzzySearch.extractOne(name, Initializer.allNames).getString();

        boolean isErrorOutputed = false;
        try {
            // Sending of the item's info
            if (Initializer.nameToItem.containsKey(name)) {
                Item item = Initializer.nameToItem.get(name);
                if (item.isPremium()) {
                    if (!Player.isPremium(event.getUser())) {
                        event.reply("The item you searched for is not there!").queue();
                        return;
                    }
                }
                if (item instanceof LimitedItems limitedItems) {
                    EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(item.getName() + " (" + Player.getItemCount(event.getUser().getIdLong(), name) + " owned)");
                    embedBuilder.setColor(Color.CYAN);
                    embedBuilder.setDescription("> " + item.getDescription() + "\n\n" +
                            "**BUY** - " + (item.getCostToBuy() == null ? "Out of Stock" : " [" + item.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")") + "\n" +
                            "**SELL** - " + " [" + item.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")\n" +
                            "**Stock** - " + (limitedItems.getCurrentStock()) + "/" + limitedItems.getMaxStock());
                    embedBuilder.addField("Type", "`Item`", true);
                    try {
                        embedBuilder.setThumbnail(item.getEmojiUrl());
                    } catch (IllegalArgumentException ignored) {
                    }
                    event.replyEmbeds(embedBuilder.build()).queue();
                    return;
                } else {
                    EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(item.getName() + " (" + Player.getItemCount(event.getUser().getIdLong(), name) + " owned)");
                    embedBuilder.setColor(Color.CYAN);
                    embedBuilder.setDescription("> " + item.getDescription() + "\n\n" +
                            "**BUY** - " + (item.getCostToBuy() == null ? "Out of Stock" : " [" + item.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")") + "\n" +
                            "**SELL** - " + " [" + item.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")");
                    embedBuilder.addField("Type", "`Item`", true);
                    try {
                        embedBuilder.setThumbnail(item.getEmojiUrl());
                    } catch (IllegalArgumentException ignored) {
                    }
                    event.replyEmbeds(embedBuilder.build()).queue();
                    return;
                }
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

    private static ArrayList<String> buildDescription(long userId) {
        // Retrieves all the items and places it in a list
        ArrayList<String> content = new ArrayList<>();
        Player player = DataUtils.getPlayer(Bot.jda.getUserById(userId));
        ArrayList<Item> itemList = Initializer.items;

        int x = 0;
        boolean premium = false;
        boolean patreon = false;
        boolean stock = false;
        boolean rank = false;
        while (x < itemList.size()) {
            Item items = itemList.get(x);
            String label;
            if (items.getRank().equals(player.getRank())) {
                if (items.isPremium()) {
                    if (Player.isPremium(userId)) {
                        label = "";
                        if (!premium) {
                            label = "**Premium:**\n";
                            premium = true;
                        }
                        content.add(label + items.getEmojiOfItem() + " **" + items.getName() + "** " +
                                "(" + Player.getItemCount(userId, (items.getName())) + ") — " + (items.getCostToBuy() == null ? "[Out of stock" : berri + " [" + formatter.format(items.getCostToBuy())) + "](" + Bot.PAYPAL_LINK + ")\n*" +
                                items.getDescription() +
                                (items instanceof LimitedItems limitedItems ?
                                        "*\n**Stock:** " + limitedItems.getCurrentStock() + "/" + limitedItems.getMaxStock() :
                                        "*") + "\n");
                    }
                } else if (items.isPatreonOnly()) {
                    if (items.isPatreonOnly() == Player.isPatreon(Bot.jda.retrieveUserById(userId).complete())) {
                        label = "";
                        if (!patreon) {
                            label = "**Patreon:**\n";
                            patreon = true;
                        }
                        content.add(label + items.getEmojiOfItem() + " **" + items.getName() + "** " +
                                "(" + Player.getItemCount(userId, (items.getName())) + ") — " + (items.getCostToBuy() == null ? "[Out of stock" : berri + " [" + formatter.format(items.getCostToBuy())) + "](" + Bot.PAYPAL_LINK + ")\n*" +
                                items.getDescription() +
                                (items instanceof LimitedItems limitedItems ?
                                        "*\n**Stock:** " + limitedItems.getCurrentStock() + "/" + limitedItems.getMaxStock() :
                                        "*") + "\n");
                    }
                } else {
                    label = "";
                    if (items instanceof LimitedItems) {
                        label = "";
                        if (!stock) {
                            label = "**Limited Items:**\n";
                            stock = true;
                        }
                    } else {
                        if (!rank) {
                            label = "**Rank:**\n";
                            rank = true;
                        }
                    }
                    content.add(label + items.getEmojiOfItem() + " **" + items.getName() + "** " +
                            "(" + Player.getItemCount(userId, (items.getName())) + ") — " + (items.getCostToBuy() == null ? "[Out of stock" : berri + " [" + formatter.format(items.getCostToBuy())) + "](" + Bot.PAYPAL_LINK + ")\n*" +
                            items.getDescription() +
                            (items instanceof LimitedItems limitedItems ?
                                    "*\n**Stock:** " + limitedItems.getCurrentStock() + "/" + limitedItems.getMaxStock() :
                                    "*") + "\n");
                }
            }
            x++;
        }

        if (content.isEmpty()) {
            content.add("Nothing is available in your shop for now");
        }

        return content;
    }
}