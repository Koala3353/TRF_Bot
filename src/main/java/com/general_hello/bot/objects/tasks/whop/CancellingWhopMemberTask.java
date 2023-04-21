package com.general_hello.bot.objects.tasks.whop;

import com.general_hello.Config;
import com.general_hello.bot.utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.TimerTask;

public class CancellingWhopMemberTask extends TimerTask {
    @Override
    public void run() {
        Util.logInfo("Running CancellingWhopMemberTask", CancellingWhopMemberTask.class);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.whop.com/api/v2/events?page=1&per=10&event_type=canceled_membership"))
                .header("accept", "application/json")
                .header("Authorization", Config.get("whop"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray data = responseJson.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                long epochSecond = Instant.now().getEpochSecond();
                JSONObject dataObject = data.getJSONObject(i);
                int created_at = dataObject.getInt("created_at");
                if ((int) epochSecond - created_at <= 60) {
                    Util.logInfo("New member cancelling", NewWhopMemberTask.class);
                    String memberId = dataObject.getString("resource_id");

                    request = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.whop.com/api/v2/memberships/" + memberId + "?expand="))
                            .header("accept", "application/json")
                            .header("Authorization", Config.get("whop"))
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    String cancelJson = response.body();
                    JSONObject cancellingMember = new JSONObject(cancelJson);
                    JSONObject custom_fields_responses = cancellingMember.getJSONObject("custom_fields_responses");
                    String email = custom_fields_responses.getString("Your email");
                    String phoneNumber = custom_fields_responses.getString("Your phone #");
                    String fullName = custom_fields_responses.getString("Your full name ");
                    Util.sendDM("856400310461267990", fullName + " is cancelling his/her subscription in Whop. These are the details:\n" +
                            "**Email:** " + email + "\n" +
                            "**Phone Number:** " + phoneNumber);
                    Util.logInfo("Successfully sent the user cancel message to Gianfranco! " + fullName, CancellingWhopMemberTask.class);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
