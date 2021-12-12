package com.general_hello.commands.commands.Work;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WorkCommand implements ICommand {
    public static HashMap<User, OffsetDateTime> cooldown = new HashMap<>();
    public static HashMap<User, Job> job = new HashMap<>();
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        User author = ctx.getAuthor();
        if (cooldown.containsKey(author)) {
            OffsetDateTime offsetDateTime = cooldown.get(author);
            if (offsetDateTime.plusHours(1).isAfter(OffsetDateTime.now())) {
                ctx.getChannel().sendMessage("Woah.... You should take a break first! Kindly wait until " + TimeFormat.RELATIVE.format(offsetDateTime.plusHours(1)) + " before working again! Also, have a coffee ☕").queue();
                return;
            }
        }

        cooldown.put(author, OffsetDateTime.now());

        Job newJob = new Job(ctx.getAuthor(), ctx.getChannel());
        newJob.makeAndSendQuestion();
        job.put(ctx.getAuthor(), newJob);
        try {
            if (ctx.getMember().getRoles().contains(ctx.getJDA().getGuildById(843769353040298011L).getRolesByName("work reminder", true).get(0))) {
                ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " Wake up!!! It is time to work!!!").queueAfter(1, TimeUnit.HOURS);
            }
        } catch (Exception ignored) {}
    }

    @Override
    public String getName() {
        return "work";
    }

    @Override
    public String getHelp(String prefix) {
        return "Work the ignite bank!!!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
