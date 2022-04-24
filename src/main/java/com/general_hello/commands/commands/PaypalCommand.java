package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class PaypalCommand extends SlashCommand {
    public PaypalCommand() {
        this.name = "paypal";
        this.help = "Show the link to buy Rainbow Shards at shop which will be using PayPal";
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }
        event.replyEmbeds(EmbedUtil.defaultEmbed("**Rainbow Shards**: [Buy here now!](" + Bot.PATREON_LINK + ")")).addActionRow(Button.link(Bot.PAYPAL_LINK, "Paypal")).queue();
    }
}
