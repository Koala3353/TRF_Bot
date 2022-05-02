package com.general_hello.commands.commands.stage1;

import com.general_hello.Bot;
import com.general_hello.commands.ButtonPaginator;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Items.Initializer;
import com.general_hello.commands.Objects.Items.Object;
import com.general_hello.commands.Objects.User.Player;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class InventoryCommand extends SlashCommand {
    /**
     * <p>This class is a command that shows the users inventory
     *
     * @author General Rain
     */
    public InventoryCommand() {
        // Setting the name, cooldown, and help
        this.name = "inventory";
        this.help = "Displays your inventory";
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "user", "The user you want to check the inventory of"));
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Checks if the user has made an account
        if (DataUtils.makeCheck(event)) {
            return;
        }
        // Stores the users and all the names of items
        User author = event.getUser();
        if (event.getOption("user") != null) {
            author = event.getOption("user").getAsUser();
        }
        ArrayList<String> allNames = new ArrayList<>(Initializer.nameToItem.keySet());
        long authorId = author.getIdLong();
        int x = 0;
        int y = 0;
        ArrayList<String> inventories = new ArrayList<>();
        StringBuilder inventory = new StringBuilder();

        // Loops through all the names and checks whether you have that item or not
        while (x < allNames.size()) {
            int itemCount = Player.getItemCount(authorId, allNames.get(x));
            if (itemCount > 0 && Initializer.nameToItem.containsKey(allNames.get(x))) {
                Object objects = Initializer.nameToItem.get((allNames.get(x)));
                inventory.append(objects.getEmojiOfItem()).append(" **")
                        .append(objects.getName()).append("** ─ ")
                        .append(itemCount).append("\n")
                        .append("*ID* `").append(DataUtils.filter(objects.getName())).append("`")
                        .append(" ─ ").append(objects.getFormattedPrice()).append("\n\n");
                y++;
            }

            if (y%5==0) {
                if (!inventory.toString().equals("")) {
                    inventories.add(inventory.toString());
                }
                inventory = new StringBuilder();
            }
            x++;
        }

        if (!inventory.toString().equals("")) {
            inventories.add(inventory.toString());
        }

        if (inventories.isEmpty()) {
            event.reply(author.getAsMention() + " does not have anything in their inventory").setEphemeral(true).queue();
            return;
        }

        // Sending the message and building the paginator
        ButtonPaginator.Builder builder = new ButtonPaginator.Builder(event.getJDA())
                .setEventWaiter(Bot.getBot().getEventWaiter())
                .setItemsPerPage(1)
                .setTimeout(1, TimeUnit.MINUTES)
                .useNumberedItems(false);

        builder.setTitle("**" + author.getName() + "**'s inventory")
                .setItems(inventories)
                .addAllowedUsers(event.getUser().getIdLong())
                .setColor(0x0099E1);

        event.reply("Loading inventory...").queue((interactionHook -> {
            interactionHook.deleteOriginal().queueAfter(3, TimeUnit.SECONDS);
        }));
        builder.build().paginate(event.getTextChannel(), 1);
    }
}
