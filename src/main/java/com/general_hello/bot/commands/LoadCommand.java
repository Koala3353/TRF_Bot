package com.general_hello.bot.commands;

import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.events.OnMemberJoin;
import com.general_hello.bot.objects.AddToEODReportUserTask;
import com.general_hello.bot.utils.Util;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static com.general_hello.bot.database.Airtable.Airtable.*;

public class LoadCommand extends SlashCommand {
    public LoadCommand() {
        this.name = "load";
        this.help = "Creates a new user in Airtable with all the members in the guild";
        this.userPermissions = new Permission[]{Permission.MANAGE_CHANNEL};
        OptionData data = new OptionData(OptionType.USER, "user", "The user to add to the Airtable").setRequired(true);
        List<OptionData> dataList = new ArrayList<>();
        dataList.add(data);
        this.options = dataList;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        // Airtable
        event.reply("Done!").setEphemeral(true).queue();

        Member member = event.getOption("user").getAsMember();
        DataUtils.newUserJoined(member.getUser().getIdLong());
        String discordTag = member.getUser().getAsTag();
        OffsetDateTime now = member.getTimeJoined();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\"fields\": {\"Discord Username\": \"" + discordTag + "\", \"Discord Join Date\": \"" +
                now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "\", \"Level\": \"Brethren\", \"DiscordName\": \"" + event.getUser().getName().replace(" ", "") + "\"}}", mediaType);
        Request request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_USERS_TABLE_ID)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            Response response = client.newCall(request).execute();

            JSONObject jsonObject = new JSONObject(response.body().string());
            String recordId = jsonObject.getString("id");
            DataUtils.newRecordId(member.getIdLong(), recordId);
            Util.logInfo("Added user to Airtable: " + discordTag, OnMemberJoin.class);

            // EOD REPORTS
            Timer timer = new Timer(true);
            timer.schedule(new AddToEODReportUserTask(client, mediaType, body, request, member.getUser().getName().replace(" ", ""), member.getUser().getIdLong(), member), 360000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
