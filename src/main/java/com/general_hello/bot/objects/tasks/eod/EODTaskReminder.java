package com.general_hello.bot.objects.tasks.eod;


import com.general_hello.Bot;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.utils.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimerTask;

public class EODTaskReminder extends TimerTask {
    Long guildId;

    public EODTaskReminder(Long guildId) {
        this.guildId = guildId;
    }

    @Override
    public void run() {
        completeTask(this.guildId);
    }

    private void completeTask(long guildId) {
        Guild guildById = Bot.getJda().getGuildById(guildId);
        List<Member> members = guildById.loadMembers().get();

        for (Member member : members) {
            if (member.getUser().isBot()) continue;

            if (Boolean.FALSE.equals(DataUtils.getBooleanFromInt(DataUtils.didAnswer(member.getIdLong())))) {
                // global time
                OffsetDateTime oneDayLater = OffsetDateTime.now(ZoneId.of("UTC-6")).withHour(0).withMinute(0).withSecond(0);
                // check if onedaylater is before now
                if (oneDayLater.isBefore(OffsetDateTime.now(ZoneId.of("UTC-6")))) {
                    oneDayLater = oneDayLater.plusDays(1);
                }
                Util.sendDM(member.getId(), "You have not answered the EOD report yet. Please do so as soon as possible. You only have " + Util.getTimestampDiscord(oneDayLater) + " left.");
                Util.logInfo("Sent EOD Reminder to " + member.getUser().getAsTag(), this.getClass());
            }
        }
    }
}