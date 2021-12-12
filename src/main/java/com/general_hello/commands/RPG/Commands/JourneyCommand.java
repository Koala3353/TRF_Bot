package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class JourneyCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(ctx.getAuthor().getName() + "'s Journey");
        embedBuilder.setDescription("Welcome " + ctx.getAuthor().getAsMention() + "\n" +
                "\n" +
                "**RPG** - Role-playing video game, electronic game genre in which players advance through a story quest, and often many side quests, for which their character or party of characters gain experience that improves various attributes and abilities.\n" +
                "\n" +
                "For the **RPG** of " + ctx.getSelfUser().getAsMention() + ", you will be a soldier of France who got stranded and lost after a fierce battle!\n" +
                "Your goal will be to go back to your home country, France. Good luck soldier!\n" +
                "\n" +
                "**Start your journey** <a:crabby:900170344202113096> - Creates a new data for RPG\n" +
                "**End your journey** <:dinosaur:905241832550699099> - Deletes all your data on RPG");
        embedBuilder.setFooter("Start your journey now!").setTimestamp(OffsetDateTime.now());

        boolean start = true;
        boolean end = true;
        int shekels = RPGUser.getShekels(ctx.getAuthor().getIdLong());

        if (shekels == -1) {
            start = false;
        } else {
            end = false;
        }
        ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRow(
                Button.of(ButtonStyle.PRIMARY, ctx.getAuthor().getId() + ":start", "Start your journey").withEmoji(Emoji.fromEmote("crabby", 900170344202113096L, true)).withDisabled(start),
                Button.of(ButtonStyle.DANGER, ctx.getAuthor().getId() + ":end", "End your journey").withEmoji(Emoji.fromEmote("dinosaur", 905241832550699099L, false)).withDisabled(end)
        ).queue();
    }

    @Override
    public String getName() {
        return "journey";
    }

    @Override
    public String getHelp(String prefix) {
        return "Starts or restarts your journey of the land of RPG";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
