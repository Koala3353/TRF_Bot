package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Config;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.HashMap;

public class DropCommand implements ICommand {
    public static HashMap<Long, Boolean> isClaimed = new HashMap<>();
    public static HashMap<Long, Button> button = new HashMap<>();
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        ctx.getMessage().delete().queue();
        if (ctx.getAuthor().getId().equals(Config.get("owner_id")) || ctx.getAuthor().getId().equals(Config.get("owner_id_partner"))) {
            ctx.getChannel().sendMessage("<@&905691492205621278>").queue();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("New Chest Drop!!!").setTimestamp(OffsetDateTime.now()).setColor(InfoUserCommand.randomColor());
            embedBuilder.setDescription("A new chest 🧰 has been found! **First igniter** to press the button below will get  <:credit:905976767821525042> **-100,000** to  <:credit:905976767821525042> **1,000,000 **!\n" +
                    "\n**Warning:** There is a possibility to be reduced in credits for at least  <:credit:905976767821525042> **100,000** 💸💸💸!!!");
            embedBuilder.setThumbnail("https://images-ext-1.discordapp.net/external/e4iDunw5XV3-Hspl7LA8XBLbTLZMQP7rVPJqMkGuMco/https/cdn.discordapp.com/emojis/861390922410360883.gif");
            ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).setActionRows(
                    ActionRow.of(Button.of(ButtonStyle.PRIMARY, "0000:claim", "Claim"), Button.of(ButtonStyle.DANGER, "0000:NADAME", "Dropped by " + ctx.getAuthor().getName()).asDisabled())
            ).queue((message -> {
                isClaimed.put(message.getIdLong(), false);
                button.put(message.getIdLong(), Button.of(ButtonStyle.DANGER, "0000:NADAME", "Dropped by " + ctx.getAuthor().getName()).asDisabled());
            }));
        }
    }

    @Override
    public String getName() {
        return "drop";
    }

    @Override
    public String getHelp(String prefix) {
        return "Drops a credit chest to users!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
