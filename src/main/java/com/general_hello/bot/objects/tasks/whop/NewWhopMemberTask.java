package com.general_hello.bot.objects.tasks.whop;

import com.general_hello.Bot;
import com.general_hello.Config;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.events.OnMemberJoin;
import com.general_hello.bot.utils.Util;
import net.dv8tion.jda.api.entities.Guild;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.TimerTask;

import static com.general_hello.bot.database.airtable.Airtable.*;

public class NewWhopMemberTask extends TimerTask {
    @Override
    public void run() {
        Util.logInfo("Running NewWhopMemberTask", NewWhopMemberTask.class);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.whop.com/api/v2/events?page=1&per=10&event_type=payment_updates"))
                .header("accept", "application/json")
                .header("Authorization", Config.get("whop"))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);
            JSONObject responseJson = new JSONObject(responseBody);
            JSONArray data = responseJson.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                long epochSecond = Instant.now().getEpochSecond();
                JSONObject dataObject = data.getJSONObject(i);
                int created_at = dataObject.getInt("created_at");
                int timeDiff = (int) epochSecond - created_at;
                if (timeDiff <= 60) {
                    Util.logInfo("New member signed up", NewWhopMemberTask.class);
                    String paymentKey = dataObject.getString("resource_id");
                    // Get payment
                    request = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.whop.com/api/v2/payments/" + paymentKey))
                            .header("accept", "application/json")
                            .header("Authorization", Config.get("whop"))
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    JSONObject paymentInfo = new JSONObject(response.body());
                    // Get member
                    request = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.whop.com/api/v2/memberships/" + paymentInfo.getString("membership") + "?expand="))
                            .header("accept", "application/json")
                            .header("Authorization", Config.get("whop"))
                            .method("GET", HttpRequest.BodyPublishers.noBody())
                            .build();
                    response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                    JSONObject member = new JSONObject(response.body());
                    // discord info
                    JSONObject discord = member.getJSONObject("discord");
                    String discordTag = discord.getString("username");
                    String username = discordTag.split("#")[0];
                    // general info
                    JSONObject custom_fields_responses = member.getJSONObject("custom_fields_responses");
                    String email = custom_fields_responses.getString("Your email");
                    String phoneNumber = custom_fields_responses.getString("Your phone #");
                    String fullName = custom_fields_responses.getString("Your full name ");
                    String firstName = fullName.split(" ")[0];
                    String lastName = fullName.split(" ")[1];

                    // promo_code
                    if (!member.isNull("promo_code")) {
                        String promoCode = member.getString("promo_code");

                        request = HttpRequest.newBuilder()
                                .uri(URI.create("https://api.whop.com/api/v2/promo_codes/" + promoCode))
                                .header("accept", "application/json")
                                .header("Authorization", Config.get("whop"))
                                .method("GET", HttpRequest.BodyPublishers.noBody())
                                .build();
                        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                        String promoCodeResponse = response.body();
                        JSONObject promoCodeObj = new JSONObject(promoCodeResponse);
                        String userId = promoCodeObj.getString("code");
                        Guild guild = Bot.getJda().getGuilds().get(0);
                        guild.addRoleToMember(Bot.getJda().getUserById(userId), guild.getRoleById("1071181977442590842")).queue();
                        Util.sendDM(userId, "Congratulations! " + fullName + " used your code! L3e is now given to you.");
                    }

                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create("{\"fields\": {\"Discord Username\": \"" + discordTag + "\", \"First Name\": \"" + firstName + "\",  \"Last Name\": \"" + lastName + "\",  \"Phone #\": \"" + phoneNumber + "\",  \"Email\": \"" + email + "\", \"Level\": \"Brethren\", \"DiscordName\": \"" + username.replace(" ", "") + "\"}}", mediaType);
                    Request airtableReq = new Request.Builder()
                            .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_USERS_TABLE_ID)
                            .post(body)
                            .addHeader("Authorization", "Bearer " + API_KEY)
                            .addHeader("Content-Type", "application/json")
                            .build();

                    Response airtableResponse = client.newCall(airtableReq).execute();

                    JSONObject jsonObject = new JSONObject(airtableResponse.body().string());
                    String recordId = jsonObject.getString("id");
                    DataUtils.newRecordId(Long.parseLong(discord.getString("id")), recordId);
                    Util.logInfo("Added user to Airtable: " + discordTag, OnMemberJoin.class);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
