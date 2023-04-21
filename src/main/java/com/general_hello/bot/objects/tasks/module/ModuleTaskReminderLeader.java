package com.general_hello.bot.objects.tasks.module;


import com.general_hello.bot.utils.Util;
import net.dv8tion.jda.api.entities.Member;

import java.util.TimerTask;

public class ModuleTaskReminderLeader extends TimerTask {
    Member member;

    public ModuleTaskReminderLeader(Member member) {
        this.member = member;
    }
    @Override
    public void run() {
        completeTask();
    }
    private void completeTask() {
        // if member doesnt have a5 role
        if (member.getRoles().stream().noneMatch(role -> role.getIdLong() == 1065381062668206100L)) {
            // send a message to the user
            String leaderId = Util.getChannelFromRole(member).getTopic().split(",")[0];
            member.getJDA().getUserById(leaderId).openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Kindly remind " + member.getEffectiveName() + " to complete the tutorials, level intro and level promotion quiz!").queue());
        }
    }
}