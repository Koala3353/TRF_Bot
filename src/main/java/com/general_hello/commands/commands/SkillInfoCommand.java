package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Skill;
import com.general_hello.commands.Objects.User.Player;
import com.general_hello.commands.Objects.User.Rank;
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

import static com.general_hello.commands.Objects.RPGEmojis.berri;

public class SkillInfoCommand extends SlashCommand {
    public static final DecimalFormat formatter = new DecimalFormat("#,###.00");
    public SkillInfoCommand() {
        this.name = "skills";
        this.help = "To show information " +
                "(description etc) of available skill in game";
        this.cooldown = 5;
        this.options = Collections.singletonList(new OptionData(OptionType.STRING, "item", "The item name to see the details of").setAutoComplete(true));
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

        if (event.getOption("item") == null) {
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

        String name = event.getOption("item").getAsString();
        name = FuzzySearch.extractOne(name, Initializer.allNames).getString();

        boolean isErrorOutputed = false;
        try {
            if (Initializer.nameToSkill.containsKey(name)) {
                Skill skill = Initializer.nameToSkill.get(name);
                EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(skill.getName() + " (" + Player.getItemCount(event.getUser().getIdLong(), name) + " owned)");
                embedBuilder.setColor(Color.GREEN);
                embedBuilder.setDescription("> " + skill.getDescription() + "\n\n" +
                        "**BUY** - " + (skill.getCostToBuy() == null ? "Out of Stock" : berri + " [" + skill.getFormattedPrice() + "](" + Bot.PAYPAL_LINK + ")") + "\n" +
                        "**SELL** - [Can't be sold](" + Bot.PAYPAL_LINK + ")\n" +
                        "**TRADE** - Unknown");
                try {
                    embedBuilder.setThumbnail(skill.getEmojiUrl());
                } catch (IllegalArgumentException ignored) {}
                embedBuilder.addField("Effects", "`" + skill.effectsString() + "`", true);
                embedBuilder.addField("Energy spent", "`" + skill.getEnergy() + "`", true);
                embedBuilder.addField("Neo Devil Fruit skill", "`" + skill.isNeodevilfruitonly() + "`", true);
                event.replyEmbeds(embedBuilder.build()).queue();
                return;
            }
        } catch (Exception e) {
            event.reply("The skill you searched for is not there!").queue();
            e.printStackTrace();
            isErrorOutputed = true;
        }

        if (!isErrorOutputed) {
            event.reply("The skill you searched for is not there!").queue();
        }
    }


    private static ArrayList<String> buildDescription(long userId) {
        ArrayList<String> content = new ArrayList<>();
        ArrayList<Skill> skills = Initializer.skills;

        Player player = DataUtils.getPlayer(Bot.jda.getUserById(userId));
        int x = 0;
        while (x < skills.size()) {
            Skill skill = skills.get(x);
            if (Rank.isHigherOrEqual(skill.getRank(), player.getRank())) {
                if (skill.isNeodevilfruitonly()) {
                    if (player.getNeoDevilFruit() > 0) {
                        if (skill.isPatreonOnly()) {
                            if (skill.isPatreonOnly() == Player.isPatreon(Bot.jda.retrieveUserById(userId).complete())) {
                                content.add(skill.getEmojiOfItem() + " **" + skill.getName() + "** " +
                                        "(" + Player.getItemCount(userId, skill.getName()) + ") — " + (skill.getCostToBuy() == null ? "[Out of stock" : berri + " [" + formatter.format(skill.getCostToBuy())) + "](" + Bot.PAYPAL_LINK + ")\n" +
                                        skill.getDescription() + "\n");
                            }
                        } else {
                            content.add(skill.getEmojiOfItem() + " **" + skill.getName() + "** " +
                                    "(" + Player.getItemCount(userId, skill.getName()) + ") — " + (skill.getCostToBuy() == null ? "[Out of stock" : berri + " [" + formatter.format(skill.getCostToBuy())) + "](" + Bot.PAYPAL_LINK + ")\n" +
                                    skill.getDescription() + "\n");
                        }
                    }
                } else {
                    content.add(skill.getEmojiOfItem() + " **" + skill.getName() + "** " +
                            "(" + Player.getItemCount(userId, skill.getName()) + ") — " + (skill.getCostToBuy() == null ? "[Out of stock" : berri + " [" + formatter.format(skill.getCostToBuy())) + "](" + Bot.PAYPAL_LINK + ")\n" +
                            skill.getDescription() + "\n");
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