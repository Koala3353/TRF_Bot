package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Objects.Level.Level;
import com.general_hello.commands.Objects.Level.Rank;
import com.general_hello.commands.Objects.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Member;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LeaderboardCommand extends SlashCommand {
    public LeaderboardCommand() {
        this.name = "leaderboard";
        this.help = "Shows the leaderboard";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        final DecimalFormat formatter = new DecimalFormat("#,###");
        List<Member> members = event.getGuild().getMembersWithRoles(
                event.getGuild().getRolesByName(Level.getRoleNameFromChannel(event.getTextChannel()), true));
        int x = 0;
        ArrayList<Integer> money = new ArrayList<>();
        HashMap<Integer, Member> hashMap = new HashMap<>();
        while (x < members.size()) {
            Member member = members.get(x);
            int shekels = new Player(member.getIdLong(), Level.getEnumNameFromChannel(event.getTextChannel())).getPoints();
            if (shekels != -1) {
                money.add(shekels);
                hashMap.put(shekels, member);
            }
            x++;
        }
        money.sort(Collections.reverseOrder());
        int y = 0;
        x = 0;
        boolean userThere = false;
        ArrayList<String> content = new ArrayList<>();
        while (y < hashMap.size()) {
            Member member = hashMap.get(money.get(y));
            Integer credits = money.get(y);
            if (!member.getUser().isBot()) {
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
                content.add(rankShow + " **" + formatter.format(credits) + " points**  (" + Rank.getRankFromPoints(credits).getName() + ") - " + member.getUser().getAsTag());
                if (member.getUser().equals(event.getUser())) {
                    userThere = true;
                }
                x++;
            }
            y++;
        }

        x = y;

        try {
            while (!userThere) {
                Member member = hashMap.get(money.get(x));
                Integer credits = money.get(x);
                if (!member.getUser().isBot()) {
                    y++;
                }
                if (member.getUser().equals(event.getUser())) {
                    content.add("😢 **" + y + "** **" + formatter.format(credits) + " points**  (" + Rank.getRankFromPoints(credits).getName() + ") - **" + member.getUser().getAsTag() + "**");
                    userThere = true;
                }
                x++;
            }
        } catch (Exception ignored) {}
        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(10)
                .setTimeout(5, TimeUnit.MINUTES)
                .useNumberedItems(false);

        builder.setTitle("**Leaderboard**")
                .setItems(content)
                .addAllowedUsers(event.getUser().getIdLong())
                .setColor(0x0099E1);
        if (content.isEmpty()) {
            event.reply("No one is on the leaderboard").queue();
            return;
        } else {
            event.reply("Loading leaderboard...").setEphemeral(true).queue();
        }
        builder.build().paginate(event.getTextChannel(), 1);
    }
}
