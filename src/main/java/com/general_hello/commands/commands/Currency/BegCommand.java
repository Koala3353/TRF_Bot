package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Bot;
import com.general_hello.commands.Config;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.Emoji.Emojis;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Utils.UtilNum;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.TimeFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class BegCommand implements ICommand {
    public static HashMap<User, OffsetDateTime> cooldown = new HashMap<>();
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        User author = ctx.getAuthor();
        if (RPGDataUtils.checkUser(ctx)) {
            return;
        }
        if (cooldown.containsKey(author)) {
            OffsetDateTime offsetDateTime = cooldown.get(author);
            if (offsetDateTime.plusMinutes(10).isAfter(OffsetDateTime.now())) {
                ctx.getChannel().sendMessage("Woah.... You should take a break from begging! Kindly wait until " + TimeFormat.RELATIVE.format(offsetDateTime.plusMinutes(10)) + " before asking for money again! Also, have pizza 🍕").queue();
                return;
            }
        }

        cooldown.put(author, OffsetDateTime.now());

        int minRobOrFine = 0;
        int maxRobOrFine = 2_500;

        int randomNum = UtilNum.randomNum(minRobOrFine, maxRobOrFine);

        DecimalFormat formatter = new DecimalFormat("#,###.00");

        if (robOrNoRob(UtilNum.randomNum(10, 50))) {
            RPGUser.addShekels(ctx.getSelfUser().getIdLong(), (randomNum));
            ctx.getChannel().sendMessage(author.getAsMention() + " " + begSuccessMessages() + " " + Emojis.shekels + " " + formatter.format(randomNum)).queue();
        } else {
            //fine stuff
            randomNum = randomNum / 4;
            RPGUser.addShekels(ctx.getSelfUser().getIdLong(), (-randomNum));

            ctx.getChannel().sendMessage(author.getAsMention() + " The duck got " + Emojis.shekels + formatter.format(randomNum) + " from your pocket! Better luck next time!!!").queue();
        }

        if (ctx.getMember().getRoles().contains(ctx.getJDA().getGuildById(843769353040298011L).getRolesByName("beg reminder", true).get(0))) {
            ctx.getChannel().sendMessage(ctx.getMember().getAsMention() + " Stop playing!!! It is time to ask for money!!!").queueAfter(10, TimeUnit.MINUTES);
        }
    }

    private boolean robOrNoRob(int rank) {
        int i = UtilNum.randomNum(0, 100);
        int max = 50+(rank/2);
        if (max > 80) {
            max = 80;
        }
        return i < max;
    }

    private String begSuccessMessages() {
        int randomNum = UtilNum.randomNum(1, 8);

        switch (randomNum) {
            case 1:
                return "Ahiya Marktee came to you and decided to give you some cash 💵. Giving you";
            case 2:
                return "You found some cash at the floor. Getting";
            case 3:
                return "A kid came up to you and said, **Can you give me some candy...** \uD83D\uDC40.... You gave him a candy and the kid gave you";
            case 4:
                return "Starbucks isn't a place to beg, the strange thing is that everyone in the room gave you some money. All in all you received";
            case 5:
                return "While going to the place where you'll beg, an old man named " + Bot.jda.getUserById(Config.get("owner_id")).getName() + " gave you";
            case 6:
                return "While begging you fell into a pool of **cash**... Bringing home a total amount of";
            case 7:
                return "While begging you caught a robber robbing the bank. As a reward, the bank gave you";
            case 8:
                return "You fell onto a pig and went to the hospital. Making the insurance company pay to you";
        }

        return "This is NOT supposed to appear if it does.... LUCKY YOU!!!";
    }

    @Override
    public String getName() {
        return "beg";
    }

    @Override
    public String getHelp(String prefix) {
        return "Beg to you, your friend, your girl friend, and the world.";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
