package com.general_hello.bot.objects.tasks.eod;


import com.general_hello.bot.database.airtable.Airtable;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

import java.util.TimerTask;

public class EODCleanupTask extends TimerTask {
    private final Member member;
    private final String value;
    private final GenericComponentInteractionCreateEvent event;
    private final User author;
    private final String[] ids;

    public EODCleanupTask(Member member, String value, GenericComponentInteractionCreateEvent event, User author, String[] ids) {
        this.member = member;
        this.value = value;
        this.event = event;
        this.author = author;
        this.ids = ids;
    }

    @Override
    public void run() {
        completeTask();
    }

    private void completeTask() {
        Airtable.runEodReportCleanup(value, event, member, author, ids);
    }
}