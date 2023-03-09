package com.general_hello.bot.objects;


import net.dv8tion.jda.api.entities.Member;

import java.util.TimerTask;

public class ModuleTaskReminder extends TimerTask {
    Member member;

    public ModuleTaskReminder(Member member) {
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
            member.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Kindly complete the tutorials, level intro and level promotion quiz!").queue());
        }
    }
}