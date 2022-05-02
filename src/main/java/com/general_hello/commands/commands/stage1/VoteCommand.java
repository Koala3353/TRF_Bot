package com.general_hello.commands.commands.stage1;

import com.general_hello.Config;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class VoteCommand extends SlashCommand {
    public VoteCommand() {
        this.name = "vote";
        this.help = "Vote the bot for rewards";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Commented out since the bot isn't in the voting site
        // Document on how to add if it is will be placed on stage 4
        //Bot.isVoted();

        // Reply with the embed
        event.replyEmbeds(EmbedUtil.defaultEmbed("**Support & Vote for the Bot by Pressing the Button below!**")).addActionRow(
                Button.link(Config.get("vote"), "Vote")
        ).queue();
    }
}
