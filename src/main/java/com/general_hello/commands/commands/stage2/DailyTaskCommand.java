package com.general_hello.commands.commands.stage2;

import com.general_hello.commands.Objects.Emojis.BotEmojis;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

public class DailyTaskCommand extends SlashCommand {
    /**
     * <p>This class is a command the shows their daily tasks
     *
     * @author General Rain
     */

    public DailyTaskCommand() {
        // Name
        this.name = "tasks";
        // Help
        this.help = "Do your daily tasks here";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Sends the message
        event.replyEmbeds(EmbedUtil.defaultEmbed("**Daily tasks: 📜**\n" +
                BotEmojis.xmark + " - **Fight a total of 250 Ai (any) battles**\n" +
                BotEmojis.xmark + " - **Win 100 Ai (any) battles**\n" +
                BotEmojis.xmark + " - **Fight a total of 50 PvP battles against other factions**\n" +
                BotEmojis.xmark + " - **Win a total of 25 PvP (any PvP combats types) battles against other factions**\n" +
                BotEmojis.xmark + " - **Complete a total of 40 Base Wall Attacks or Repairs**\n" +
                BotEmojis.xmark + " - **Do 35 Profession commands**\n" +
                BotEmojis.xmark + " - **Claim your Daily Bento from Bento Traveler 1 time**\n" +
                BotEmojis.xmark + " - **Fight and win against World Boss 1 time**\n\n" +
                "> Vote the bot to claim the rewards.")).queue();
    }
}
