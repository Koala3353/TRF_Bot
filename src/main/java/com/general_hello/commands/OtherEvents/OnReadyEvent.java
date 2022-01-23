package com.general_hello.commands.OtherEvents;

import com.general_hello.commands.Bot;
import com.general_hello.commands.commands.Currency.CommonDrop;
import com.general_hello.commands.commands.Marriage.MarriageData;
import com.general_hello.commands.commands.Others.UpdateIgniteCoinsCommand;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OnReadyEvent extends ListenerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnReadyEvent.class);

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Bot.jda = event.getJDA();

        try {
            LOGGER.info("Starting to retrieve Ignite coins balance...");
            UpdateIgniteCoinsCommand.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CommonDrop.runDrop();
        MarriageData.runDeduct();
        LOGGER.info("Started the auto drop and auto deduct marriage xp.");
    }
}
