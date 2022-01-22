package com.general_hello.commands.RPG.Commands;

import com.general_hello.commands.RPG.Items.Initializer;
import com.general_hello.commands.RPG.Objects.Animal;
import com.general_hello.commands.RPG.RpgUser.RPGDataUtils;
import com.general_hello.commands.RPG.RpgUser.RPGUser;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class CookCommand extends Command {
    public CookCommand() {
        this.name = "cook";
        this.cooldown = 5;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (RPGDataUtils.checkUser(event)) {
            return;
        }

        if (RPGUser.getHealth(event.getAuthor().getIdLong()) == 100) {
            event.reply("You can't cook some food when your health is 100 <:heal:912615109430493194>.");
            return;
        }

        if (event.getArgs().isEmpty()) {
            event.replyError("Kindly place what item you'll cook, and the quantity (Optional)\n" +
                    "`ignt cook puffer 5` or `ignt cook puffer`");
            return;
        }

        String[] args = event.getArgs().split("\\s+");
        String itemName = args[0];
        if (!Initializer.animals.contains(RPGDataUtils.filter(itemName))) {
            event.replyError("You cannot cook a non-animal!!! <:cook_command:923392113033506817>");
            return;
        }

        int amount = 1;

        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (Exception e) {
                event.replyError("Invalid number placed! Try again!");
                return;
            }
        }

        if (amount < 1) {
            event.replyError("Don't try to do the hacks 😒");
            return;
        }
        long authorId = event.getAuthor().getIdLong();
        if (RPGUser.getItemCount(authorId, RPGDataUtils.filter(itemName)) < amount) {
            event.reply("You do not have enough food!");
            return;
        }

        Animal animal = Initializer.allAnimals.get(RPGDataUtils.filter(itemName));
        int rewardForCooking = animal.getRewardForCooking() * amount;
        int health = RPGUser.getHealth(authorId);
        int finalHealth = health + rewardForCooking;

        if (finalHealth > 100) {
            finalHealth = 100;
        }

        RPGUser.setHealth(authorId, finalHealth);
        RPGUser.addItem(authorId, -amount, RPGDataUtils.filter(itemName));
        event.reply("<:cook_command:923392113033506817> You cooked " + amount + " " + animal.getName() + " and earned <:heal:912615109430493194> " + rewardForCooking + " health. In total, your health <:heal:912615109430493194> is " + finalHealth + ".");
    }
}
