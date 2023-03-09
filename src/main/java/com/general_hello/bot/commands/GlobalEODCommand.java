package com.general_hello.bot.commands;

import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.EODTask;
import com.general_hello.bot.utils.EODUtil;
import com.general_hello.bot.utils.Util;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;

public class GlobalEODCommand extends SlashCommand {
    public GlobalEODCommand() {
        this.name = "globaleod";
        this.help = "Sends an EOD report to all users in the server";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
    }

    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        Guild guildById = slashCommandEvent.getGuild();
        List<Member> members = guildById.loadMembers().get();
        for (Member member : members) {
            if (member.getUser().isBot()) {
                Util.logInfo("Skipping bot " + member.getUser().getAsTag(), EODTask.class);
                return;
            }
            if (Boolean.FALSE.equals(DataUtils.getBooleanFromInt(DataUtils.didAnswer(member.getIdLong())))) {
                DataUtils.setReportStreak(member.getIdLong(), 0);
                DataUtils.incrementMissedDays(member.getIdLong());
                DataUtils.setRelapseFreeStreak(member.getIdLong(), 0);
            }

            DataUtils.setDidAnswer(member.getIdLong(), false);
            DataUtils.setIsEODAnswered(member.getIdLong(), false);

            EODUtil.newDailyEODReport(member);
            Util.logInfo("EOD Report for " + member.getUser().getAsTag() + " has been sent.", EODTask.class);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        slashCommandEvent.reply("EOD reports have been sent.").queue();
    }
}
