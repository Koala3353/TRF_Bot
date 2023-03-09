package com.general_hello.bot.objects;


import com.general_hello.bot.database.Airtable.Airtable;
import com.general_hello.bot.database.DataUtils;
import net.dv8tion.jda.api.entities.Member;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.TimerTask;

import static com.general_hello.bot.database.Airtable.Airtable.*;

public class AddToEODReportUserTask extends TimerTask {
    private OkHttpClient client;
    private MediaType mediaType;
    private RequestBody body;
    private Request request;
    private final String discordTag;
    private final long userId;

    public AddToEODReportUserTask(OkHttpClient client, MediaType mediaType, RequestBody body, Request request, String discordTag, long userId, Member member) {
        this.client = client;
        this.mediaType = mediaType;
        this.body = body;
        this.request = request;
        this.discordTag = discordTag.replace(" ", "");
        this.userId = userId;
        System.out.println("Task Made");
    }

    @Override
    public void run() {
        try {
            completeTask();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void completeTask() throws IOException {
        System.out.println("Running task");
        client = new OkHttpClient();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create("{\"fields\": {\"TRF 2.0 Users\": [\"" + Airtable.getRecordIdInEODReportFromEODUser(discordTag) + "\"]}}", mediaType);
        request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + EOD_BASE_ID + "/" + EOD_OVERVIEW)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        String response = client.newCall(request).execute().body().string();
        System.out.println(response);
        JSONObject jsonObject = new JSONObject(response);
        String recordId = jsonObject.getString("id");
        DataUtils.setOverviewId(userId, recordId);

        try {
            System.out.println("Sleeping for 6 minutes");
            Thread.sleep(360000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client = new OkHttpClient();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create("{\"fields\": {\"TRF 2.0 Users\": [\"" + Airtable.getRecordIdInEODReportFromEODUser(discordTag) + "\"], \"EOD Overview\": [\"" + Airtable.getRecordIdInEODOverview(discordTag) + "\"]}}", mediaType);
        request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + EOD_BASE_ID + "/" + EOD_LOG)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        System.out.println(client.newCall(request).execute().body().string());

        client = new OkHttpClient();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create("{\"fields\": {\"TRF 2.0 Users\": [\"" + Airtable.getRecordIdInEODReportFromEODUser(discordTag) + "\"], \"EOD Overview\": [\"" + Airtable.getRecordIdInEODOverview(discordTag) + "\"]}}", mediaType);
        request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + EOD_BASE_ID + "/" + EOD_Q1)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        System.out.println(client.newCall(request).execute().body().string());

        client = new OkHttpClient();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create("{\"fields\": {\"TRF 2.0 Users\": [\"" + Airtable.getRecordIdInEODReportFromEODUser(discordTag) + "\"], \"EOD Overview\": [\"" + Airtable.getRecordIdInEODOverview(discordTag) + "\"]}}", mediaType);
        request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + EOD_BASE_ID + "/" + EOD_Q2)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        System.out.println(client.newCall(request).execute().body().string());

        client = new OkHttpClient();
        mediaType = MediaType.parse("application/json");
        body = RequestBody.create("{\"fields\": {\"TRF 2.0 Users\": [\"" + Airtable.getRecordIdInEODReportFromEODUser(discordTag) + "\"], \"EOD Overview\": [\"" + Airtable.getRecordIdInEODOverview(discordTag) + "\"]}}", mediaType);
        request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + EOD_BASE_ID + "/" + EOD_Q3)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        System.out.println(client.newCall(request).execute().body().string());
    }
}