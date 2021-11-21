package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.commands.CommandContext;
import com.general_hello.commands.commands.CommandType;
import com.general_hello.commands.commands.ICommand;
import com.general_hello.commands.commands.Info.InfoUserCommand;
import com.general_hello.commands.commands.Register.Data;
import com.general_hello.commands.commands.User.UserPhoneUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class LeaderboardCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException, IOException, SQLException {
        try {
            HashMap<User, UserPhoneUser> userUserPhoneUserHashMap = Data.userUserPhoneUserHashMap;
            ArrayList<User> users = Data.users;
            ArrayList<UserPhoneUser> userPhoneUsers = new ArrayList<>();
            int z = 0;
            while (z < users.size()) {
                User user = users.get(z);
                if (userUserPhoneUserHashMap.containsKey(user)) {
                    userPhoneUsers.add(userUserPhoneUserHashMap.get(user));
                }
                z++;
            }

            List<UserPhoneUser> collectedUsers = userPhoneUsers.stream().sorted(UserPhoneUser::compareTo).collect(Collectors.toList());

            List<User> toFilter = new ArrayList<>();

            int x = 0;
            while (x < collectedUsers.size()) {
                toFilter.add(collectedUsers.get(x).getDiscordUser());
                x++;
            }

            Set<User> set = new LinkedHashSet<>(toFilter);

            toFilter.clear();
            toFilter.addAll(set);

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Leaderboard of Credits").setFooter("Who is the richest of the richest?").setTimestamp(OffsetDateTime.now());
            StringBuilder stringBuilder = new StringBuilder();

            int y = 0;
            x = 0;
            while (y < userUserPhoneUserHashMap.size()) {
                UserPhoneUser credits = userUserPhoneUserHashMap.get(toFilter.get(y));
                if (!credits.getDiscordUser().isBot()) {
                    int rank = x + 1;
                    String rankShow;

                    if (rank == 1) {
                        rankShow = "🥇";
                    } else if (rank == 2) {
                        rankShow = "🥈";
                    } else if (rank == 3) {
                        rankShow = "🥉";
                    } else {
                        rankShow = "🔹";
                    }

                    DecimalFormat formatter = new DecimalFormat("#,###.00");
                    Integer credits1 = credits.getCredits();
                    stringBuilder.append(rankShow).append(" <:credit:905976767821525042> **").append(formatter.format(credits1)).append("** - ").append(credits.getDiscordUser().getAsTag()).append("\n");

                    if (y == 20) {
                        break;
                    }
                    x++;
                }
                y++;
            }

            embedBuilder.setDescription(stringBuilder.toString());
            embedBuilder.setColor(InfoUserCommand.randomColor());
            embedBuilder.setThumbnail("https://images-ext-1.discordapp.net/external/CldQTLK4UezxcAi3qvvrGrFCFa-1aFY_Miz5czSDPdY/https/cdn.discordapp.com/emojis/716848179022397462.gif");

            ctx.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "lb";
    }

    @Override
    public List<String> getAliases() {
        ArrayList<String > aliases = new ArrayList<>();
        aliases.add("leaderboard");
        aliases.add("richest");
        return aliases;
    }

    @Override
    public String getHelp(String prefix) {
        return "Shows you the richest of the richest!!!";
    }

    @Override
    public CommandType getCategory() {
        return CommandType.SPECIAL;
    }
}
