package com.general_hello.commands.commands.stage2;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Others.Achievement;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.general_hello.commands.Objects.User.Player.hasUnlockedAchievement;

public class AchievementCommand extends SlashCommand {
    /**
     * <p>This class is a command the shows the users achievement and whether the unlocked it or not
     *
     * @author General Rain
     */
    public AchievementCommand() {
        // name of the command
        this.name = "achievement";
        // help info
        this.help = "To see all achieved in game achievements.";
        // cooldown of the command
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // This checks whether they made an account or not
        if (DataUtils.makeCheck(event)) {
            return;
        }

        // Gets all the possible achievements of the bot
        ArrayList<Achievement> achievements = Initializer.achievements;
        // The arraylist that has the String that will show the fancy text
        ArrayList<String> achievementsList = new ArrayList<>();
        // User id of the user who used the command
        long userId = event.getUser().getIdLong();
        // Just looping through the achievement and checks whether the user did it or not
        for (Achievement achievement : achievements) {
            String achievementName = achievement.getName();
            if (hasUnlockedAchievement(userId, achievementName)) {
                achievementsList.add("**" + achievementName + "** - `[Completed ✅]`");
            } else {
                achievementsList.add("**???** `[Not Completed ❌]` - ???");
            }
        }
        // Makes the button paginator
        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(5) // Amount of achievements shown per page
                .setTimeout(1, TimeUnit.MINUTES) // Timeout of buttons
                .useNumberedItems(false);

        // Setting of title, color, and content
        builder.setTitle("Achievements")
                .setItems(achievementsList)
                .addAllowedUsers(event.getUser().getIdLong())
                .setColor(0x0099E1);

        // Sending the message
        event.reply("Loading your achievements...").queue((interactionHook -> {
            interactionHook.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
        }));
        builder.build().paginate(event.getTextChannel(), 1);
    }
}
