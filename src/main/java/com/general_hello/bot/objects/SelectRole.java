package com.general_hello.bot.objects;

import com.general_hello.Config;
import com.general_hello.bot.utils.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SelectRole {
    // json file
    public static void send(JDA jda) {
        // Create a JSON parser
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("roles.json"));
            JSONArray list = (JSONArray) obj;
            long channelId = Config.getLong("role_selection_channel");
            StringSelectMenu.Builder menu = StringSelectMenu.create("0000:selectroles")
                    .setPlaceholder("Choose your group")
                    .setRequiredRange(1, 1);
            for (Object o : list) {
                JSONObject option = (JSONObject) o;
                String title = (String) option.get("title");
                String desc = (String) option.get("description");
                long roleId = Long.parseLong((String) option.get("role_id"));
                menu.addOption(title, String.valueOf(roleId), desc);
            }

            Guild guild = jda.getGuilds().get(0);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("It's time to join your weekly accountability group");
            embedBuilder.setDescription("Please choose from below the best time you are AVAILABLE to meet for your accountability group’s weekly call. Choose wisely as you cannot change the time once selected!");
            embedBuilder.setImage("https://lh5.googleusercontent.com/h3YGYd4Yg54bwVtOfj5zdE6mXG21Zn6T31LDZLNS-2u54UT0t6Juk7P9EIGGbvDDgaE=w2400");
            embedBuilder.setFooter(guild.getName(), guild.getIconUrl());
            embedBuilder.setColor(Color.YELLOW);

            guild.getTextChannelById(channelId).sendMessageEmbeds(embedBuilder.build()).setActionRow(menu.build()).queue();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public static void remove(long roleId) {
        JSONParser parser = new JSONParser();
        Object file;
        try {
            file = parser.parse(new FileReader("roles.json"));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        if (file == null) return;
        JSONArray jsonArray = (JSONArray) file;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject obj = (JSONObject) jsonArray.get(i);
            Object role_id = obj.get("role_id");
            if ((role_id).equals(String.valueOf(roleId))) {
                jsonArray.remove(i);
                break;
            }
        }

        try (FileWriter fileWriter = new FileWriter("data.json");
             BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write(jsonArray.toJSONString());
            Util.logInfo("JSON file updated successfully", SelectRole.class);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}



