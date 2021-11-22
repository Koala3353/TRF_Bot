package com.general_hello.commands.commands.Currency;

import com.general_hello.commands.Bot;
import com.general_hello.commands.Database.DatabaseManager;
import com.general_hello.commands.Database.SQLiteDataSource;
import com.general_hello.commands.commands.Register.Data;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaxGetter {
    public static void runTaxGetter() {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(TaxGetter::run, 0, 1, TimeUnit.DAYS);
    }

    private static void run() {
        ArrayList<User> blacklist = new ArrayList<>();
        ArrayList<User> users = Data.users;
        int x = 0;
        while (x < users.size()) {
            User user = users.get(x);
            if (!blacklist.contains(user)) {
                blacklist.add(user);
                if (SQLiteDataSource.totalReceived.containsKey(user.getIdLong())) {
                    Integer tax = SQLiteDataSource.totalReceived.get(user.getIdLong());
                    DatabaseManager.INSTANCE.setCredits(user.getIdLong(), (-tax));
                    Bot.jda.getTextChannelById(862860646248087602L).sendMessage(user.getName() + " paid the tax worth " + tax).queue();
                }
            }
            x++;
        }

        SQLiteDataSource.totalReceived = new HashMap<>();
    }
}
