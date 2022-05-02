package com.general_hello.commands.commands.stage2;

import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.Emojis.RPGEmojis;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Collections;

public class ProfileCommand extends SlashCommand {
    public ProfileCommand() {
        this.name = "profile";
        this.help = "Check own Profile or other players Profile";
        this.cooldown = 10;
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "user", "The user you want to check the profile of"));
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (DataUtils.makeCheck(event)) {
            return;
        }

        // Gets the user
        User user = event.getUser();
        if (event.getOption("user") != null) {
            user = event.getOption("user").getAsUser();
        }


        // Checks if they made an account or not
        if (DataUtils.hasAccount(user) == -1) {
            event.reply(user.getAsMention() + " didn't made an account still.").queue();
            return;
        }

        // Get the player
        Player player = DataUtils.getPlayer(user);

        // Sets the color, title, footer, and thumbnail
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setTitle(user.getName() + "'s Profile");
        embedBuilder.setThumbnail(user.getEffectiveAvatarUrl());
        embedBuilder.setFooter("Like their profile by pressing the button below!",
                event.getJDA().getSelfUser().getEffectiveAvatarUrl());

        // Builds their info
        if (event.getUser().getId().equals(user.getId())) {
            embedBuilder.setDescription("**Name:** " + user.getName() + "\n" +
                    "**Level:** " + getLevel(player.getExp()) + " (" + player.getExp() + " EXP)\n\n" +
                    "**Total AIs beaten:** 0\n" +
                    "**PvP win/lose record:** 0 wins 0 loss\n" +
                    "**Ranked matches win/lose record:** 0 wins 0 loss (Leaderboard Position: UNKNOWN)\n" +
                    "**Tournament win/lose record:** 0 wins 0 loss\n" +
                    "**Bounty:** None\n\n" +
                    "**Berri:** " + RPGEmojis.berri + " " + player.getBerri() + "\n" +
                    "**Rainbow shards:** " + RPGEmojis.rainbowShards + " " + player.getRainbowShardsTotal() + "\n" +
                    "**Neo Devil Fruit:** None\n" +
                    "**Rx:** " + player.getRank().getMarineName() + "\n" +
                    "**Profession:** None\n\n" +
                    "**Achievement Title:** None\n" +
                    "**Likes:** " + player.getLikes() + "\n" +
                    "**Married to:** None\n" +
                    "**Sensei or Student:** " + (player.getSenseiUser() == null ? "None" : player.getSenseiUser().getName()));
        } else {
            embedBuilder.setDescription("**Name:** " + user.getName() + "\n" +
                    "**Level:** " + getLevel(player.getExp()) + " (" + player.getExp() + " EXP)\n\n" +
                    "**Total AIs beaten:** 0\n" +
                    "**PvP win/lose record:** 0 wins 0 loss\n" +
                    "**Ranked matches win/lose record:** 0 wins 0 loss (Leaderboard Position: UNKNOWN)\n" +
                    "**Tournament win/lose record:** 0 wins 0 loss\n" +
                    "**Bounty:** None\n\n" +
                    "**Neo Devil Fruit:** None\n" +
                    "**Rx:** " + player.getRank().getMarineName() + "\n" +
                    "**Profession:** None\n\n" +
                    "**Achievement Title:** None\n" +
                    "**Likes:** " + player.getLikes() + "\n" +
                    "**Married to:** None\n" +
                    "**Sensei or Student:** " + (player.getSenseiUser() == null ? "None" : player.getSenseiUser().getName()));
        }
        // Sends the message
        event.replyEmbeds(embedBuilder.build())
                .addActionRow(Button.secondary(user.getId() + ":profile", "Like")
                        .withEmoji(Emoji.fromMarkdown("👍"))).queue();
    }

    /**
     * @param xp The xp
     * @return The level
     */
    public static int getLevel(long xp)
    {
        if (xp < 100) return 0;
        int counter = 0;
        long total = 0L;
        while (true)
        {
            long neededForNextLevel = getXPToLevelUp(counter);
            if (neededForNextLevel > xp) return counter;
            total += neededForNextLevel;
            if (total > xp) return counter;
            counter++;
        }
    }

    /**
     * returns the relative XP needed to level up to the next level
     *
     * @param currentLevel the current level
     * @return relative XP needed to level up
     */
    public static long getXPToLevelUp(int currentLevel)
    {
        double x = 5 * (currentLevel * currentLevel) + (50 * currentLevel) + 100;
        return (long) x;
    }
}
