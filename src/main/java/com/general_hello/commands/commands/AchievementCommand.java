package com.general_hello.commands.commands;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Achievement;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.general_hello.commands.Objects.User.Player.hasUnlockedAchievement;

public class AchievementCommand extends SlashCommand {
    public AchievementCommand() {
        this.name = "achievement";
        this.help = "To see all achieved in game achievements.";
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }
        ArrayList<Achievement> achievements = Initializer.achievements;
        ArrayList<String> achievementsList = new ArrayList<>();
        long userId = event.getUser().getIdLong();
        for (Achievement achievement : achievements) {
            String achievementName = achievement.getName();
            if (hasUnlockedAchievement(userId, achievementName)) {
                achievementsList.add("**" + achievementName + "** - `[Completed ✅]`");
            } else {
                achievementsList.add("**???** `[Not Completed ❌]` - ???");
            }
        }
        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(5)
                .setTimeout(1, TimeUnit.MINUTES)
                .useNumberedItems(false);

        builder.setTitle("Achievements")
                .setItems(achievementsList)
                .addAllowedUsers(event.getUser().getIdLong())
                .setColor(0x0099E1);

        event.reply("Loading your achievements...").queue((interactionHook -> {
            interactionHook.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
        }));
        builder.build().paginate(event.getTextChannel(), 1);
    }
}
