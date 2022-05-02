 package com.general_hello.commands.OtherEvents;

 import com.general_hello.commands.Items.Initializer;
 import com.general_hello.commands.Objects.Object;
 import com.general_hello.commands.Objects.RPGEmojis;
 import com.general_hello.commands.Objects.Trade;
 import me.xdrop.fuzzywuzzy.FuzzySearch;
 import net.dv8tion.jda.api.EmbedBuilder;
 import net.dv8tion.jda.api.entities.Emoji;
 import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
 import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
 import net.dv8tion.jda.api.hooks.ListenerAdapter;
 import net.dv8tion.jda.api.interactions.components.ActionRow;
 import net.dv8tion.jda.api.interactions.components.Modal;
 import net.dv8tion.jda.api.interactions.components.buttons.Button;
 import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
 import net.dv8tion.jda.api.interactions.components.text.TextInput;
 import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
 import org.jetbrains.annotations.NotNull;

 import java.util.ArrayList;
 import java.util.List;

 public class OnSelectionMenu extends ListenerAdapter {
    @Override
    public void onSelectMenuInteraction(@NotNull SelectMenuInteractionEvent event) {
        // Checks if the selection menu id starts with trade
        if (event.getSelectMenu().getId().startsWith("trade")) {
            String userId = event.getSelectMenu().getId().split(":")[1];
            if (event.getUser().getId().equals(userId)) {
                switch (event.getSelectedOptions().get(0).getValue()) {
                    // If add items is selected it'll send a modal
                    case "additems" -> {
                        TextInput items = TextInput.create("additems", "Add Items", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("Items to add. Place a [, ] to add multiple items")
                                .setMinLength(4)
                                .setMaxLength(300)
                                .setRequired(true)
                                .build();
                        Modal modal = Modal.create("additems", "Add items")
                                .addActionRows(ActionRow.of(items))
                                .build();
                        event.replyModal(modal).queue();
                        // If change items is selected it'll send a modal
                    } case "changeitems" -> {
                        TextInput items = TextInput.create("changeitems", "Change Items", TextInputStyle.PARAGRAPH)
                                .setPlaceholder("Items to place. Place a [, ] to add multiple items")
                                .setMinLength(4)
                                .setMaxLength(500)
                                .setRequired(true)
                                .build();
                        Modal modal = Modal.create("changeitems", "Reset and place items")
                                .addActionRows(ActionRow.of(items))
                                .build();
                        event.replyModal(modal).queue();
                        // If add berri is selected it'll send a modal
                    } case "addberri" -> {
                        TextInput items = TextInput.create("addberri", "Set Berri", TextInputStyle.SHORT)
                                .setPlaceholder("Place the amount of Berri to place on the deal")
                                .setMinLength(1)
                                .setMaxLength(8)
                                .setRequired(true)
                                .build();
                        Modal modal = Modal.create("addberri", "Set Berri")
                                .addActionRows(ActionRow.of(items))
                                .build();
                        event.replyModal(modal).queue();
                        // If add rainbow shards is selected it'll send a modal
                    } case "addrainbowshards" -> {
                        TextInput items = TextInput.create("addrainbowshards", "Set Rainbow Shards", TextInputStyle.SHORT)
                                .setPlaceholder("Place the amount of Rainbow Shards to place on the deal")
                                .setMinLength(1)
                                .setMaxLength(8)
                                .setRequired(true)
                                .build();
                        Modal modal = Modal.create("addrainbowshards", "Set Rainbow Shards")
                                .addActionRows(ActionRow.of(items))
                                .build();
                        event.replyModal(modal).queue();
                    }
                }
            } else {
                event.reply("You can't press this.").setEphemeral(true).queue();
            }
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        switch (event.getModalId()) {
            case "additems" -> {
                // It'll get the content you placed on the modal and add it to the selected offer
                long userId = Long.parseLong(event.getUser().getId());
                String items = event.getValue("additems").getAsString();
                String[] itemsList = items.split(", ");
                List<Object> itemsOffered = new ArrayList<>();
                for (String itemName : itemsList) {
                    String itemNameChosen = FuzzySearch.extractTop(itemName, Initializer.nameToItem.keySet(), 1).get(0).getString();
                    itemsOffered.add(Initializer.nameToItem.get(itemNameChosen));
                }
                EmbedBuilder embedBuilder = Trade.loadTrade(userId)
                        .getFocusedOffer().addItems(itemsOffered)
                        .save(userId)
                        .buildEmbed();
                sendEmbed(event, embedBuilder);
            } case "changeitems" -> {
                // It'll get the content you placed on the modal and reset the offer then add it to the selected offer
                long userId = Long.parseLong(event.getUser().getId());
                String items = event.getValue("changeitems").getAsString();
                String[] itemsList = items.split(", ");
                List<Object> itemsOffered = new ArrayList<>();
                for (String itemName : itemsList) {
                    String itemNameChosen = FuzzySearch.extractTop(itemName, Initializer.nameToItem.keySet(), 1).get(0).getString();
                    itemsOffered.add(Initializer.nameToItem.get(itemNameChosen));
                }
                EmbedBuilder embedBuilder = Trade.loadTrade(userId)
                        .getFocusedOffer().setItems(itemsOffered)
                        .save(userId)
                        .buildEmbed();
                sendEmbed(event, embedBuilder);
            } case "addberri" -> {
                // It'll get the amount of berri you placed on the modal and add it to the selected offer
                long userId = Long.parseLong(event.getUser().getId());
                int amount;
                try {
                    amount = Integer.parseInt(event.getValue("addberri").getAsString());
                } catch (Exception e) {
                    event.reply("Invalid number placed!").queue();
                    return;
                }
                EmbedBuilder embedBuilder = Trade.loadTrade(userId)
                        .getFocusedOffer().setBerri(amount)
                        .save(userId)
                        .buildEmbed();
                sendEmbed(event, embedBuilder);
            } case "addrainbowshards" -> {
                // It'll get the amount of rainbow shards you placed on the modal and add it to the selected offer
                long userId = Long.parseLong(event.getUser().getId());
                int amount;
                try {
                    amount = Integer.parseInt(event.getValue("addrainbowshards").getAsString());
                } catch (Exception e) {
                    event.reply("Invalid number placed!").queue();
                    return;
                }
                EmbedBuilder embedBuilder = Trade.loadTrade(userId)
                        .getFocusedOffer().setRainbowShards(amount)
                        .save(userId)
                        .buildEmbed();
                sendEmbed(event, embedBuilder);
            }
        }
    }

    private void sendEmbed(ModalInteractionEvent event, EmbedBuilder embedBuilder) {
        // The default trade menu
        SelectMenu menu = SelectMenu.create("trade:" + event.getUser().getId())
                .setPlaceholder("Choose the option")
                .setRequiredRange(1, 1)
                .addOption("Add Items", "additems", "Adds items for the trade", Emoji.fromMarkdown("<:egyptian_broken_weapon:905969748582469682>"))
                .addOption("Change Items", "changeitems", "Resets your offer of items and adds items", Emoji.fromMarkdown("<a:loading:870870083285712896>"))
                .addOption("Add Berri", "addberri", "Resets the berri and place your offer for it", Emoji.fromMarkdown(RPGEmojis.berri))
                .addOption("Add Rainbow Shards", "addrainbowshards", "Resets the rainbow shards and place your offer for it", Emoji.fromMarkdown(RPGEmojis.rainbowShards))
                .build();
        event.replyEmbeds(embedBuilder.build())
                .addActionRows(
                        ActionRow.of(menu),
                        ActionRow.of(
                                Button.secondary(event.getUser().getId() + ":switch", "Switch Editing Offer").withEmoji(Emoji.fromMarkdown("<:right:915425310592356382>")),
                                Button.danger(event.getUser().getId() + ":reset", "Reset Trade").withEmoji(Emoji.fromMarkdown("<:xmark:957420842898292777>")),
                                Button.success(event.getUser().getId() + ":submit", "Submit Trade").withEmoji(Emoji.fromMarkdown("<:check:957420541256531969>"))
                        )
                ).queue();
    }
}