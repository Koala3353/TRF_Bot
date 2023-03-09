package com.general_hello.bot.commands;

import com.general_hello.bot.database.DataUtils;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class AddQuestion extends SlashCommand {
    public AddQuestion() {
        this.name = "addquestion";
        this.help = "Add a question to your EOD report (Max of 10)";
        this.guildOnly = false;
        OptionData data = new OptionData(OptionType.STRING, "question", "The question to ask").setRequired(true);
        List<OptionData> dataList = new ArrayList<>();
        dataList.add(data);
        this.options = dataList;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        String question = event.getOption("question").getAsString();
        if (question.length() > 150) {
            event.reply("The question given is too long! " + question.length() + " characters was given! (150 characters is the maximum)").setEphemeral(true).queue();
            return;
        }
        DataUtils.addQuestions(event.getUser().getIdLong(), question);
        event.reply("Question added!").setEphemeral(true).queue();
    }
}
