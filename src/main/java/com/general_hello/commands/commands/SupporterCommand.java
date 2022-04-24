package com.general_hello.commands.commands;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.RPGEmojis;
import com.general_hello.commands.Objects.User.Player;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class SupporterCommand extends SlashCommand {
    public SupporterCommand() {
        this.name = "supporter";
        this.help = "Show the amount of Rainbow Shards that players have totally accumulated";
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }
        Player player = DataUtils.getPlayer(event.getUser());
        event.replyEmbeds(EmbedUtil.defaultEmbed("**Rainbow Shards**: " + RPGEmojis.rainbowShards + " " + player.getRainbowShards())).addActionRow(Button.primary("hi", "Go to the shop to get some").asDisabled()).queue();
    }
}
