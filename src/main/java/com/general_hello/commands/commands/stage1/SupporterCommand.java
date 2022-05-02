package com.general_hello.commands.commands.stage1;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.Emojis.RPGEmojis;
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
        // Gets the player and checks their rainbow shards and sends the message that contains their info
        Player player = DataUtils.getPlayer(event.getUser());
        event.replyEmbeds(EmbedUtil.defaultEmbed(
                "**Rainbow Shards (Paid)**: " + RPGEmojis.rainbowShards + " " + player.getRainbowShardsBought() + "\n" +
                "**Rainbow Shards (Obtained)**: " + RPGEmojis.rainbowShards + " " + player.getRainbowShards() + "\n" +
                "**Total Rainbow Shards**: " + RPGEmojis.rainbowShards + " " + (player.getRainbowShards() + player.getRainbowShardsBought())))
                .addActionRow(Button.primary("hi", "Go to the shop to get some").asDisabled()).queue();
    }
}
