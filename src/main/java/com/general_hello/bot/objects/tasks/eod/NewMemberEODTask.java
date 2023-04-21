package com.general_hello.bot.objects.tasks.eod;


import com.general_hello.bot.utils.EODUtil;
import net.dv8tion.jda.api.entities.Member;

import java.util.TimerTask;

public class NewMemberEODTask extends TimerTask {
    Member member;

    public NewMemberEODTask(Member member) {
        this.member = member;
    }

    @Override
    public void run() {
        completeTask(this.member);
    }

    private void completeTask(Member member) {
        EODUtil.newMemberEODReport(member);
    }
}