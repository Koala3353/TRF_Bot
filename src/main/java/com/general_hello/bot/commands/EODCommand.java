package com.general_hello.bot.commands;

import com.general_hello.bot.utils.EODUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class EODCommand extends SlashCommand {
    public EODCommand() {
        this.name = "eod";
        this.help = "Sends an EOD report to a user";
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        OptionData data = new OptionData(OptionType.USER, "user", "The user to send an EOD report to").setRequired(true);
        List<OptionData> dataList = new ArrayList<>();
        dataList.add(data);
        this.options = dataList;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        Member member = event.getOption("user").getAsMember();

        EODUtil.newDailyEODReport(member);
        event.reply("Kindly check your DMs").setEphemeral(true).queue();
    }
}
