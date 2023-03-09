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
        completeTask(member.getGuild().getIdLong());
    }
    private void completeTask(long guildId) {
        // if member doesnt have a5 role
        if (member.getRoles().stream().noneMatch(role -> role.getIdLong() == 0)) {
            // add a5 role
            member.getGuild().addRoleToMember(member, member.getGuild().getRoleById(1065381062668206100L)).queue();
        }
    }
}