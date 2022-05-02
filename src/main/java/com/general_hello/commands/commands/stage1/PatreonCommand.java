package com.general_hello.commands.commands.stage1;

import com.general_hello.Bot;
import com.general_hello.Config;
import com.general_hello.commands.Database.DataUtils;
import com.general_hello.commands.Objects.User.Player;
import com.general_hello.commands.Utils.EmbedUtil;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class PatreonCommand extends SlashCommand {
    public PatreonCommand() {
        this.name = "patreon";
        this.help = "Show the link to subscribe to Patreon.";
        this.cooldown = 10;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Checks if the user made an account or not
        if (DataUtils.makeCheck(event)) {
            return;
        }

        // Checks if they're patreon
        if (Player.isPatreon(event)) {
            // Loops through the roles
            List<Role> tierRoles = event.getJDA().getGuildById(Config.get("server")).getMember(event.getUser()).getRoles();
            String tier = "None";
            for (Role role : tierRoles) {
                if (Bot.patreonRoles.contains(role.getIdLong())) {
                    tier = role.getName();
                }
            }
            // Sends their tier
            event.replyEmbeds(EmbedUtil.defaultEmbed("Patreon Tier: **" + tier + "**")).addActionRow(Button.success("extra", "You already subscribed").asDisabled()).queue();
        } else {
            // They don't have patreon so it sends this instead
            event.replyEmbeds(EmbedUtil.defaultEmbed("Subscribe & Support us by using **Patreon**!")).addActionRow(Button.link(Bot.PATREON_LINK, "Patreon")).queue();
        }
    }
}
