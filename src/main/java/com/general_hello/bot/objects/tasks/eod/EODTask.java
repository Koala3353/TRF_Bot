package com.general_hello.bot.objects.tasks.eod;


import com.general_hello.Bot;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.utils.EODUtil;
import com.general_hello.bot.utils.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.TimerTask;

public class EODTask extends TimerTask {
    Long guildId;

    public EODTask(Long guildId) {
        this.guildId = guildId;
    }

    @Override
    public void run() {
        completeTask(this.guildId);
    }

    private void completeTask(Long guildId) {
        Guild guildById = Bot.getJda().getGuildById(guildId);
        List<Member> members = guildById.loadMembers().get();
        for (Member member : members) {
            if (member.getUser().isBot()) {
                Util.logInfo("Skipping bot " + member.getUser().getAsTag(), EODTask.class);
                continue;
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}