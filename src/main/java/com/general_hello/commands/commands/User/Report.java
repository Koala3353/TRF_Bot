package com.general_hello.commands.commands.User;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Report {
    private final TextChannel sourceChannelOfReport;
    private final User authorOfMessage;
    private final User reporter;
    private final Message message;

    public Report(TextChannel sourceChannelOfReport, User authorOfMessage, User reporter, Message message) {
        this.sourceChannelOfReport = sourceChannelOfReport;
        this.authorOfMessage = authorOfMessage;
        this.reporter = reporter;
        this.message = message;
    }

    public TextChannel getSourceChannelOfReport() {
        return sourceChannelOfReport;
    }

    public User getAuthorOfMessage() {
        return authorOfMessage;
    }

    public User getReporter() {
        return reporter;
    }

    public Message getMessage() {
        return message;
    }
}
