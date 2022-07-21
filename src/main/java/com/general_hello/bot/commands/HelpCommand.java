package com.general_hello.bot.commands;

import com.general_hello.Bot;
import com.general_hello.bot.ButtonPaginator;
import com.general_hello.bot.database.DataUtils;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("ConstantConditions")
public class HelpCommand extends SlashCommand {
    private static final ArrayList<String> pages = new ArrayList<>();
    public HelpCommand() {
        this.name = "help";
        this.help = "Shows all the commands of the bot";
        this.userPermissions = new Permission[]{
                Permission.MANAGE_SERVER,
                Permission.MESSAGE_MANAGE
        };
        this.cooldown = 60;
    }

    static {
        pages.add("""
                **Commands:**
                </register:988971535731666964>: Register an account
                </info:991176902637256747>: Get the info of a game
                
                **How to use:**
                >>> To use a slash command you need to use the prefix `/` followed by the command name.
                Another way is that you can simply press the command name above, such as </help:996577180442173441>.
                """);

        pages.add("""
                ***Menu:***
                `Follow`: Follow a champ and whenever the champ makes a post, you will get a notification through your DMs.
                `Unfollow`: Unfollow a champ, you will stop getting a notification from that champ.
                
                **How to use:**
                >>> **To use the menu follow the steps below:**
                1.) Right click on a message that contains the champ you want to follow/unfollow.
                2.) Click on apps
                3.) Click on the text "Follow" or "Unfollow"
                """);

        pages.add("""
                **How to place a bet:**
                **1.)** To place a bet, you first need to have an account. If you have an account skip to step 5.
                **2.)** To make an account you need to use the command </register:988971535731666964>.
                **3.)** Input your Solana Address on the modal that pops up.
                **4.)** Press Submit.
                **5.)** To place a bet, you need to go to the channel of the sport you want to bet in.
                **6.)** Once there, you can see a message which has all the details of a game.
                **7.)** Find the game you want to bet on.
                **8.)** Upon finding it, click on the button that has the name of the team you want to bet on.
                **9.)** Once a confirmation pops up, that's it you're done!
                """);
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        if (!DataUtils.hasAccount(event.getUser().getIdLong())) {
            event.reply("Kindly make an account first by using `/register`").setEphemeral(true).queue();
            return;
        }

        if (DataUtils.isBanned(event.getUser().getIdLong())) {
            event.reply("You are banned from using the bot.").setEphemeral(true).queue();
            return;
        }
        event.reply("Please wait while I prepare the help page.").setEphemeral(true).queue();

        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA());
        builder.setColor(event.getGuild().getSelfMember().getColor());
        builder.setItemsPerPage(1);
        builder.setFooter("Press the buttons below to navigate between the pages");
        builder.setEventWaiter(Bot.getBot().getEventWaiter());
        builder.setTitle("Help");
        builder.setTimeout(365, TimeUnit.DAYS);
        builder.setItems(pages);
        builder.useNumberedItems(false);
        builder.build().paginate(event.getChannel().asTextChannel(), 1);
    }
}
