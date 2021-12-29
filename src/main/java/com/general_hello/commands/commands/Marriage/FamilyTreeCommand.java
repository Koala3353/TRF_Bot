package com.general_hello.commands.commands.Marriage;

import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.OffsetDateTime;

public class FamilyTreeCommand extends Command {
    public FamilyTreeCommand() {
        this.name = "family";
        this.cooldown = 10;
        this.cooldownScope = CooldownScope.USER;
    }

    @Override
    protected void execute(CommandEvent event) {
        User author = event.getAuthor();
        if (!event.getMessage().getMentionedUsers().isEmpty()) {
            author = event.getMessage().getMentionedUsers().get(0);
        }
        long authorIdLong = author.getIdLong();

        if (MarriageData.getWife(authorIdLong) == -1) {
            event.reply("You don't even have a family 🤪");
            return;
        }

        long wifeIdLong = MarriageData.getWife(authorIdLong);
        User wife = event.getJDA().getUserById(wifeIdLong);
        long sonIdLong = MarriageData.getSon(authorIdLong);
        User son = event.getJDA().getUserById(sonIdLong);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor(author.getName() + "'s Family", null, author.getAvatarUrl());
        embedBuilder.setDescription("Wife/Husband: **" + wife.getName() + "** (" + wife.getAsMention() + ")\n\n\n\n" +
                "Son: **" + (son != null ? son.getName():"None") +  "** (" + (son != null ? son.getAsMention():"None") + ")");
        embedBuilder.setThumbnail(wife.getAvatarUrl());
        embedBuilder.setImage((son != null ? son.getAvatarUrl():null));
        embedBuilder.setColor(InfoUserCommand.randomColor());
        embedBuilder.setFooter("Happy or sad family?").setTimestamp(OffsetDateTime.now());
        event.reply(embedBuilder.build());
    }
}
