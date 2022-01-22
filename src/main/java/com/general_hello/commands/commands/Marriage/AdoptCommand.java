package com.general_hello.commands.commands.Marriage;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;

public class AdoptCommand extends Command {
    public AdoptCommand() {
        this.name = "adopt";
        this.cooldown = 50;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getMessage().getMentionedUsers().isEmpty()) {
            event.replyError("Kindly state who you will adopt!");
            return;
        }

        User father = event.getAuthor();
        long fatherIdLong = father.getIdLong();

        if (MarriageData.getWife(fatherIdLong) == -1) {
            event.reply("You can't adopt a son/daughter without marrying!");
            return;
        }

        User son = event.getMessage().getMentionedUsers().get(0);

        if (son.isBot()) {
            event.reply("You cannot adopt a bot!");
            return;
        }

        long sonIdLong = son.getIdLong();
        long wifeIdLong = MarriageData.getWife(fatherIdLong);
        if (wifeIdLong == sonIdLong || fatherIdLong == sonIdLong) {
            event.reply("You cannot adopt your own wife or yourself ;-;");
            return;
        }

        if (MarriageData.getSon(fatherIdLong) != -1 || MarriageData.getSon(wifeIdLong) != -1) {
            event.reply("You already have a son/daughter ;-;");
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Pending Confirmation").setDescription(father.getAsMention() + " is asking for you to be his/her son/daughter!\n" +
                "**What is your answer?**");
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.danger(son.getId() + ":noAdopt:" + father.getId(), "No"),
                Button.success(son.getId() + ":yesAdopt:" + father.getId(), "Yes")
        ).queue();
    }
}
