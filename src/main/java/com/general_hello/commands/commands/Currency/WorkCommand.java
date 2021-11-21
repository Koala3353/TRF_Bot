package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.Emoji.Emojis;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.RankingSystem.LevelPointManager;
import com.general_hello.commands.commands.Register.Data;
import com.general_hello.commands.commands.User.UserPhoneUser;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WorkCommand implements ICommand {
    public static HashMap<User, OffsetDateTime> cooldown = new HashMap<>();
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

        int i = (int) LevelPointManager.calculateLevel(author);

        if (OffsetDateTime.now().getDayOfWeek().name().equalsIgnoreCase("sunday")) {
            i = 200;
        }

        UserPhoneUser bankUser = Data.userUserPhoneUserHashMap.get(ctx.getSelfUser());
        int bankCredits = bankUser.getCredits();

        int minRobOrFine = 0;
        int maxRobOrFine = 200_000;

        if (maxRobOrFine > bankCredits) {
            maxRobOrFine = bankCredits;
        }

        int randomNum = UtilNum.randomNum(minRobOrFine, maxRobOrFine);

        DecimalFormat formatter = new DecimalFormat("#,###.00");
        DatabaseManager.INSTANCE.setCredits(author.getIdLong(), randomNum);
        DatabaseManager.INSTANCE.setCredits(ctx.getSelfUser().getIdLong(), (-randomNum));
        if (randomNum == maxRobOrFine) {
            ctx.getChannel().sendMessage(author.getAsMention() + " Your boss is PROUD of you. 🤑\n" +
                    "Your salary for this not so illegal job is " + Emojis.credits + formatter.format(randomNum)).queue();
            return;
        }

        ctx.getChannel().sendMessage(author.getAsMention() + " Your boss is QUITE satisfied!\n" +
                "Your salary for this not so illegal job is " + Emojis.credits + formatter.format(randomNum)).queue();

        if (ctx.getMember().getRoles().contains(ctx.getJDA().getGuildById(843769353040298011L).getRolesByName("work reminder", true).get(0))) {
            ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " Wake up!!! It is time to work!!!").queueAfter(1, TimeUnit.HOURS);
        }
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
