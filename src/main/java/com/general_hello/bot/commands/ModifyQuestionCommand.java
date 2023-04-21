package com.general_hello.bot.commands;

import com.general_hello.bot.database.DataUtils;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class ModifyQuestionCommand extends SlashCommand {
    public ModifyQuestionCommand() {
        this.name = "modifyquestion";
        this.help = "Modifies the custom question from your EOD report";
        this.guildOnly = false;
        this.options = List.of(new OptionData[]{
                new OptionData(OptionType.STRING, "newquestion", "The question you want it to be").setRequired(true),
                new OptionData(OptionType.INTEGER, "question", "The question number you want to modify").setRequired(true)
        });
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        List<String> questionsList = DataUtils.getQuestionsList(event.getUser().getIdLong());

        int question = 0;
        String newQuestion = event.getOption("newquestion").getAsString();

        try {
            question = event.getOption("question").getAsInt();
        } catch (Exception e) {
            event.reply("Please specify a question number!").setEphemeral(true).queue();
            return;
        }

        if (question < 1) {
            event.reply("Please specify a question number greater than 0!").setEphemeral(true).queue();
            return;
        }

        if (question > questionsList.size()) {
            event.reply("Please specify a question number that is less than or equal to the number of questions you have!").setEphemeral(true).queue();
            return;
        }

        questionsList.set(question - 1, newQuestion);
        DataUtils.setQuestions(event.getUser().getIdLong(), DataUtils.getStringFromListOfQuestions(questionsList));
        event.reply("Successfully modified question number " + question + "!").setEphemeral(true).queue();
    }
}
