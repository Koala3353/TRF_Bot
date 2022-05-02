package com.general_hello.commands.commands.stage1;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class StartCommand extends SlashCommand {
    public StartCommand() {
        this.name = "start";
        this.help = "Press /start to start your adventure now!";
        this.cooldown = 60;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.hasAccount(event) != -1) {
            event.reply("You already made an account!").setEphemeral(true).queue();
            return;
        }
        // Makes a new account
        Initializer.newUser(event.getUser().getIdLong());
        // Sends the embed
        event.replyEmbeds(EmbedUtil.defaultEmbed("Welcome to Land Of Pirates!",
                "This is where you start your journey at the peaceful era, 30 years after " +
                "**Luffy** has obtained the *One Piece Treasure* and everything has calmed down, where all *Pirates* and " +
                "*Marines* has made peace with each other and amongst themselves. Be a menacing and ruthless *Outlaw* " +
                "and cause fear to both *Pirates* and *Marines*, including your own Outlaws brethren. Be a *Pirates* that do " +
                "justice towards all evildoers or be a Corrupted Marines that give information to *Outlaws* to destroy the " +
                "Pirates, rekindle hatred for each other once again and destroy them forever. The decision is up to you to make; the game world is up to you to figure out and navigate, be adventurous as you can be. Get ready to jump into the " +
                "world of **Legend Of Pirates RPG**!",
                "https://cdn.discordapp.com/attachments/962870603944194050/964031458459148308/Jumpstarting_Adventure.gif",
                "https://cdn.discordapp.com/attachments/962870603944194050/964352863029559327/Introductory_Icon.gif"))
                .addActionRow(Button.success("hi", "Successfully made an account").asDisabled()).queue();
    }
}
