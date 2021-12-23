package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Objects.RPGEmojis;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;

public class AddShekelsCommand extends Command {
    public AddShekelsCommand() {
        this.name = "addshekels";
        this.cooldown = 10;
        this.ownerCommand = true;
        this.cooldownScope = CooldownScope.USER;
    }
    @Override
    protected void execute(CommandEvent event) {
        if (event.getMessage().getMentionedMembers().isEmpty()) {
            event.getChannel().sendMessage("Kindly mention someone! `ignt addshekels 100 @Koala`").queue();
            return;
        }

        int money;
        Member member = event.getMessage().getMentionedMembers().get(0);

        String args = event.getArgs();
        String[] split = args.split("\\s+");
        try {
            money = Integer.parseInt(split[0]);
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Kindly send a valid number!").queue();
            return;
        }

        RPGUser.addShekels(member.getIdLong(), money);
        event.reply("Adding " + RPGEmojis.shekels + RPGDataUtils.formatter.format(money) + " to " + member.getAsMention());
    }
}
