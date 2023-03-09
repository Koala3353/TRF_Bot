package com.general_hello.bot.commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class NewHierarchicalRolesCommand extends SlashCommand {
    public NewHierarchicalRolesCommand() {
        this.name = "hierarchicalroles";
        this.help = "Creates a new hierarchical roles system";
        this.guildOnly = true;
        Permission[] permissions = new Permission[1];
        permissions[0] = Permission.MESSAGE_MANAGE;
        this.userPermissions = permissions;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        TextInput subject = TextInput.create("giveroleid", "Role Given ID", TextInputStyle.SHORT)
                .setPlaceholder("The role ID of the role that will be given")
                .setMinLength(17)
                .setMaxLength(22)
                .build();
        TextInput body = TextInput.create("requiredroleid", "Required Role ID", TextInputStyle.SHORT)
                .setPlaceholder("Separate each role ID with a comma (NO SPACE)")
                .setMinLength(17)
                .build();

        Modal modal = Modal.create("hierarchicalRoles", "Hierarchical Roles")
                .addActionRows(ActionRow.of(subject), ActionRow.of(body))
                .build();

        event.replyModal(modal).queue();
    }
}
