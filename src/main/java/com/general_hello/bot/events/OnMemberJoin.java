package com.general_hello.bot.events;

import com.general_hello.Bot;
import com.general_hello.Config;
import com.general_hello.bot.database.DataUtils;
import com.general_hello.bot.objects.HierarchicalRoles;
import com.general_hello.bot.objects.tasks.eod.AddToEODReportUserTask;
import com.general_hello.bot.objects.tasks.module.ModuleTaskReminder;
import com.general_hello.bot.utils.Util;
import com.general_hello.bot.utils.UtilNum;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.general_hello.bot.database.airtable.Airtable.*;

public class OnMemberJoin extends ListenerAdapter {
    private static final HashMap<Long, OffsetDateTime> timeInVc = new HashMap<>();
    private static final HashMap<Long, OffsetDateTime> lastJoinedAcc = new HashMap<>();
    private static final HashMap<Long, OffsetDateTime> lastJoinedCoach = new HashMap<>();
    private static final HashMap<Long, Integer> expEarnedCoach = new HashMap<>();
    private static final HashMap<Long, Integer> expEarnedAcc = new HashMap<>();


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        // TODO: Unlock when paid
        // Send EOD report
        /*Util.logInfo("New member joined: " + event.getMember().getUser().getAsTag() + " attempting to message...", OnMemberJoin.class);
        Member member = event.getMember();
        if (member.getUser().isBot()) return;
        DataUtils.newUserJoined(member.getUser().getIdLong());
        String discordTag = member.getUser().getAsTag();
        OffsetDateTime now = member.getTimeJoined();
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\"fields\": {\"Discord Username\": \"" + discordTag + "\", \"Discord Join Date\": \"" +
                now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth() + "\"}}", mediaType);
        Request request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_USERS_TABLE_ID + "/" + DataUtils.getRecordId(member.getIdLong()))
                .patch(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            System.out.println(client.newCall(request).execute().body().string());
            // EOD REPORTS
            Timer timer = new Timer(true);
            timer.schedule(new AddToEODReportUserTask(client, mediaType, body, request, member.getUser().getName().replace(" ", ""), event.getUser().getIdLong(), member), 780000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Timer timer = new Timer(true);
        ModuleTaskReminder moduleTaskReminder = new ModuleTaskReminder(event.getMember());
        timer.schedule(moduleTaskReminder, 86400000L);*/

        // Send EOD report
        Util.logInfo("New member joined: " + event.getMember().getUser().getAsTag() + " attempting to message...", OnMemberJoin.class);
        Member member = event.getMember();
        if (member.getUser().isBot()) return;
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
            timer.schedule(new AddToEODReportUserTask(client, mediaType, body, request, member.getUser().getName().replace(" ", ""), event.getUser().getIdLong(), member), 780000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Timer timer = new Timer(true);
        ModuleTaskReminder moduleTaskReminder = new ModuleTaskReminder(event.getMember());
        timer.schedule(moduleTaskReminder, 86400000L);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        int randomExp = UtilNum.randomNum(1, 5);
        DataUtils.addExpByText(event.getAuthor().getIdLong(), randomExp);
        String discordTag = event.getAuthor().getAsTag();
        Util.logInfo("Added " + randomExp + " exp to " + discordTag, OnMemberJoin.class);
        Bot.setJda(event.getJDA());

        if (event.getChannel().getId().equals("1062100368235966659")) {
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181441070796871")).queue();
        }

        if (event.getChannel().getId().equals("1065675704693424248")) {
            if (!event.getMessage().getAttachments().isEmpty()) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071182065447477369")).queue();
            }
        }

        if (event.getChannelType().isThread()) {
            if (event.getChannel().asThreadChannel().getParentChannel().getId().equals("1065311057003958353")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181676283170866")).queue();
            }
        }

        /*EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Please follow these rules below:");
        embedBuilder.setDescription("""
                1. Be respectful, civil, and welcoming.
                2. You will be removed from the Discord if any discrimination takes place, race, religion, gender, etc.
                3. Please do not DM me or any of the other admins in the server as we will not respond (More details on how to get support coming soon)
                4. No inappropriate or unsafe content.
                5. Do not misuse or spam in any of the channels.
                6. Do not join the server to promote your content or another server you are apart of.
                7. Any content that is NSFW (Not safe for work) is not allowed under any circumstances.
                8. Do not buy/sell/trade/give away anything.
                9. The primary language of this server is English. (Please don’t use any other languages as some won’t understand)
                10. Discord names and avatars must be appropriate.
                11. Spamming in any form is not allowed.
                12. Controversial topics such as politics are not allowed.
                13. Do not attempt to bypass any blocked words.
                14. No Discord server invite links or codes.
                15. Do not advertise without permission.
                16. Raiding is not allowed.""");
        embedBuilder.setColor(Color.YELLOW);
        embedBuilder.setFooter("Thank you for reading these rules and we hope you enjoy your stay!", event.getGuild().getIconUrl());
        EmbedBuilder secondEmbed = new EmbedBuilder();
        secondEmbed.setTitle("If you agree to the following rules click the green button below to gain access to the server!");
        secondEmbed.setColor(Color.YELLOW);
        FileUpload file = FileUpload.fromData(new File("myFile.png"), "image.png");
        event.getChannel().sendFiles(file)
                .setEmbeds(embedBuilder.build(), secondEmbed.build())
                .setActionRow(Button.success("0000:B1roleVerification", Emoji.fromFormatted("✅"))).queue();*/
    }

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        String newName = event.getUser().getName();
        String userTag = event.getUser().getAsTag();
        newName = newName.replace(" ", "");

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String json = "{\"fields\": {\"Discord Username\": \"" + userTag + "\", \"DiscordName\": \"" + newName + "\"}}";
        System.out.println(json);
        RequestBody body = RequestBody.create(json, mediaType);
        String recordId = DataUtils.getRecordId(event.getUser().getIdLong());

        Request request = new Request.Builder()
                .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_USERS_TABLE_ID + "/" + recordId)
                .patch(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
        try {
            System.out.println(client.newCall(request).execute().body().string());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();

        if (event.getChannelJoined() != null) {
            AudioChannelUnion channelJoined = event.getChannelJoined();
            String parentCategoryId = channelJoined.getParentCategoryId();
            if (parentCategoryId == null) return;
            if (!parentCategoryId.equals("1062100366583410701") && !parentCategoryId.equals("1080271533534101546")  && !channelJoined.getId().equals("1062100367942373444")) {
                return;
            }
        }
        Set<Long> membersInVc = timeInVc.keySet();

        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("UTC-6"));
        if (membersInVc.contains(member.getIdLong())) {
            OffsetDateTime offsetDateTime = timeInVc.get(member.getIdLong());
            if (offsetDateTime.isBefore(now.minusMinutes(1))) {
                long minutes = offsetDateTime.until(now, ChronoUnit.MINUTES);
                int exp = (int) (5 * minutes);
                AudioChannelUnion channelJoined = event.getChannelJoined();
                if (channelJoined == null) {
                    channelJoined = event.getChannelLeft();
                }

                if (channelJoined == null) return;

                String parentCategoryId = channelJoined.getParentCategoryId();
                if (parentCategoryId.equals("1062100366583410701") || parentCategoryId.equals("1080271533534101546")) {
                    Integer expEarnedAccForTheTime = expEarnedAcc.getOrDefault(event.getMember().getIdLong(), 0);
                    // check if lastJoinedAccOrDefault is less than a week ago
                    if (lastJoinedAcc.containsKey(event.getMember().getIdLong())) {
                        if (lastJoinedAcc.get(event.getMember().getIdLong()).isAfter(now.minusDays(6))) {
                            return;
                        } else {
                            lastJoinedAcc.remove(event.getMember().getIdLong());
                        }
                    }
                    if (expEarnedAccForTheTime >= 1) {
                        exp = 0;
                        lastJoinedAcc.put(member.getIdLong(), now);
                    }
                    if (exp + expEarnedAccForTheTime >= 1) {
                        exp = 1;
                        lastJoinedAcc.put(member.getIdLong(), now);
                    }
                    expEarnedAcc.put(member.getIdLong(), expEarnedAccForTheTime + exp);
                    DataUtils.addExpAccBy(member.getIdLong(), exp);
                    timeInVc.remove(member.getIdLong());
                    Util.logInfo("Added " + exp + " exp to " + member.getUser().getAsTag() + " for joining an accountability call.", OnMemberJoin.class);
                } else if (channelJoined.getId().equals("1062100367942373444") || channelJoined.getId().equals("1083267158924087316")) {
                    Integer expEarnedCoachForTheTime = expEarnedCoach.getOrDefault(event.getMember().getIdLong(), 0);
                    // check if lastJoinedAccOrDefault is less than a week ago
                    if (lastJoinedCoach.containsKey(event.getMember().getIdLong())) {
                        if (lastJoinedCoach.get(event.getMember().getIdLong()).isAfter(now.minusHours(20))) {
                            return;
                        } else {
                            lastJoinedCoach.remove(event.getMember().getIdLong());
                        }
                    }
                    if (expEarnedCoachForTheTime >= 1) {
                        exp = 0;
                        lastJoinedCoach.put(member.getIdLong(), now);
                    }
                    if (exp + expEarnedCoachForTheTime >= 1) {
                        exp = 1;
                        lastJoinedCoach.put(member.getIdLong(), now);
                    }
                    expEarnedCoach.put(member.getIdLong(), expEarnedCoachForTheTime + exp);
                    DataUtils.addExpBy(member.getIdLong(), exp);
                    timeInVc.remove(member.getIdLong());
                    Util.logInfo("Added " + exp + " exp to " + member.getUser().getAsTag() + " for joining a coaching call.", OnMemberJoin.class);
                }
            }
        }

        if (event.getChannelJoined() != null) {
            timeInVc.put(member.getIdLong(), now);
            Util.logInfo("Added " + member.getUser().getAsTag() + " to the timeInVc HashMap.", OnMemberJoin.class);
        }

        if (DataUtils.getExp(member.getIdLong()) >= 3) {
            event.getGuild().addRoleToMember(member, event.getGuild().getRoleById("1071181625590825062")).queue();
        }

        if (DataUtils.getAccExp(member.getIdLong()) >= 2) {
            event.getGuild().addRoleToMember(member, event.getGuild().getRoleById("1071181655424897115")).queue();
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;

        String emojiName;
        try {
            emojiName = event.getEmoji().asUnicode().getAsCodepoints();
        } catch (Exception e) {
            return;
        }
        if (emojiName.equalsIgnoreCase(Config.get("challenge"))) {
            DataUtils.newChallengeAcceptor(event.getMessageIdLong(), event.getUserIdLong());
            Util.logInfo("Added " + event.getUser().getAsTag() + " to the challenge acceptors list.", OnMemberJoin.class);

            if (event.getChannel().getId().equals("1065672746467594240")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181828553179147")).queue();
            }

            if (event.getChannel().getId().equals("1065672226772357211")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181927941410828")).queue();
            }

            if (event.getChannel().getId().equals("1065675763082350612")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181952591339611")).queue();
            }

            if (event.getChannel().getId().equals("1065675704693424248")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071182065447477369")).queue();
            }

            if (event.getChannel().getId().equals("1065672746467594240")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181828553179147")).queue();
            }

            if (event.getChannel().getId().equals("1065672226772357211")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181927941410828")).queue();
            }

            if (event.getChannel().getId().equals("1065675763082350612")) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181952591339611")).queue();
            }
        }
    }

    @Override
    public void onGuildMemberRoleAdd(@NotNull GuildMemberRoleAddEvent event) {
        List<Long> hierarchicalRoles = HierarchicalRoles.hierarchicalRoles();
        for (Long hierarchicalRoleLong : hierarchicalRoles) {
            Role hierarchicalRole = event.getGuild().getRoleById(hierarchicalRoleLong);
            List<Long> requiredRolesForRole = HierarchicalRoles.getRequiredRolesForRole(hierarchicalRoleLong);
            // check if I have all the required roles
            AtomicInteger hasRole = new AtomicInteger();
            int reqRoles = 0;
            AtomicBoolean donecheck = new AtomicBoolean(false);
            for (Long requiredRoleLong : requiredRolesForRole) {
                Role requiredRole = event.getGuild().getRoleById(requiredRoleLong);
                event.getMember().getRoles().forEach((role -> {
                    if (!donecheck.get()) {
                        if (role.getIdLong() == requiredRole.getIdLong()) {
                            hasRole.getAndIncrement();
                            donecheck.set(true);
                        }
                    }
                }));
                reqRoles++;
                donecheck.set(false);
            }
            if (hasRole.get() == reqRoles) {
                if (!event.getMember().getRoles().contains(hierarchicalRole)) {
                    event.getGuild().addRoleToMember(event.getMember(), hierarchicalRole).queue();
                    Util.logInfo("Added " + hierarchicalRole.getName() + " to " + event.getMember().getUser().getAsTag(), OnMemberJoin.class);
                }
            }
        }

        // Airtable
        event.getRoles().forEach((role -> {
            String roleNameLowerCase = role.getName().toLowerCase();
            if (roleNameLowerCase.startsWith("level")) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.get("application/json");
                String json = "{\"fields\": {\"Level\": \"" + role.getName() + "\"}}";
                RequestBody body = RequestBody.create(json, mediaType);

                Request request = new Request.Builder()
                        .url("https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_USERS_TABLE_ID + "/" + DataUtils.getRecordId(event.getMember().getIdLong()))
                        .patch(body)
                        .addHeader("Authorization", "Bearer " + API_KEY)
                        .addHeader("Content-Type", "application/json")
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (!response.isSuccessful()) {
                        try {
                            throw new Exception("Unexpected code " + response);
                        } catch (Exception e) {
                            Util.logError(e.getMessage(), OnMemberJoin.class);
                        }
                    }

                    System.out.println(response.body().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (roleNameLowerCase.startsWith("r") && roleNameLowerCase.endsWith("access")) {
                String channelMention = "NOT FOUND *REPORT THIS TO THE DEVELOPER TEAM*";
                if (roleNameLowerCase.startsWith("r1")) {
                    channelMention = event.getGuild().getTextChannelsByName("⚠relapse-help", true).get(0).getAsMention();
                } else if (roleNameLowerCase.startsWith("r2")) {
                    channelMention = event.getGuild().getTextChannelsByName("\uD83D\uDD04relapse-reset", true).get(0).getAsMention();
                }

                Util.sendDM(event.getUser().getId(), "Sounds like you had a rough day, sending you some support resources through the new relapse channels (" + channelMention + ") you've unlocked");
            } else if (roleNameLowerCase.startsWith("ag")) {
                try {
                    // Airtable for AG
                    String apiUrl = "https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_AG_LEADERS_TABLE_ID + "?filterByFormula=AGLeader=" + role.getName().replace("AG", "");
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(apiUrl)
                            .header("Authorization", "Bearer " + API_KEY)
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseBody = response.body().string();

                    JSONObject json = new JSONObject(responseBody);
                    JSONArray records = json.getJSONArray("records");
                    if (records.length() > 0) {
                        JSONObject firstRecord = records.getJSONObject(0);
                        String recordId = firstRecord.getString("id");
                        apiUrl = "https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_AG_LEADERS_TABLE_ID + "/" + recordId;
                        client = new OkHttpClient();
                        request = new Request.Builder()
                                .url(apiUrl)
                                .header("Authorization", "Bearer " + API_KEY)
                                .get()
                                .build();
                        response = client.newCall(request).execute();
                        responseBody = response.body().string();
                        json = new JSONObject(responseBody);
                        String[] users = new String[100];
                        JSONObject fields = json.getJSONObject("fields");
                        if (!fields.isNull("Assigned Users")) {
                            JSONArray fieldValue = fields.getJSONArray("Assigned Users");
                            for (int i = 0; i < fieldValue.length(); i++) {
                                users[i] = fieldValue.getString(i);
                            }
                        }

                        client = new OkHttpClient();

                        MediaType mediaType = MediaType.get("application/json");
                        StringBuilder jsonString = new StringBuilder("{\"fields\": {\"Assigned Users\": [\"" +
                                DataUtils.getRecordId(event.getUser().getIdLong()) + "\"");

                        for (String user : users) {
                            if (user == null) continue;
                            jsonString.append(", \"").append(user).append("\"");
                        }
                        jsonString.append("]}}");
                        RequestBody body = RequestBody.create(jsonString.toString(), mediaType);
                        apiUrl = "https://api.airtable.com/v0/" + BASE_ID + "/" + TRF_AG_LEADERS_TABLE_ID + "/" + recordId;
                        request = new Request.Builder()
                                .url(apiUrl)
                                .patch(body)
                                .addHeader("Authorization", "Bearer " + API_KEY)
                                .addHeader("Content-Type", "application/json")
                                .build();

                        client.newCall(request).execute();

                        TextChannel agChannel = event.getGuild().getTextChannelsByName("\uD83D\uDC40ag" + roleNameLowerCase.replace("ag", ""), false).get(0);
                        String leaderId = agChannel.getTopic().split(",")[0];
                        Util.sendDM(leaderId, "You've been assigned a new user, " + event.getUser().getAsMention() + " to your group (" + event.getUser().getId() + ")");
                        agChannel.sendMessage("Welcome to the group, " + event.getUser().getAsMention() + "! You've been assigned to " + event.getGuild().getMemberById(leaderId).getAsMention() + " as your leader.").queue();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if (roleNameLowerCase.endsWith("z") && !roleNameLowerCase.startsWith("vz")) {
                String levelNumber = roleNameLowerCase.replace("l", "").replace("z", "");
                int lvlNum = 0;
                try {
                    lvlNum = Integer.parseInt(levelNumber);
                    if (lvlNum == -1) {
                        lvlNum = 0;
                    }
                } catch (NumberFormatException e) {
                    Util.logError("Error parsing level number", OnMemberJoin.class);
                }
                List<TextChannel> textChannelsByName = event.getGuild().getTextChannelsByName("level-" + (lvlNum+1) + "-promotion", true);
                String channelMention = "*Channel not found*";
                if (textChannelsByName.isEmpty()) {
                    Util.logError("Level promotion channel not found: " + (lvlNum+1), OnMemberJoin.class);
                } else {
                    channelMention = textChannelsByName.get(0).getAsMention();
                }
                Util.sendDM(event.getUser().getId(), "Congrats, you've completed the requirements for Level " + lvlNum + ", check the " + channelMention + " channel and complete the promotion quiz to advance to level " + (lvlNum+1) + "!");
            }
        }));

        if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181441070796871")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181482003021954")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181504727765103"))) {
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181530346569869")).queue();
        }

        if (event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181566103003297")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181601419038850")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181625590825062")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181655424897115")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071181676283170866")) && event.getMember().getRoles().contains(event.getGuild().getRoleById("1071184103162003547"))) {
            event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById("1071181682234888202")).queue();
        }

        if (event.getMember().getRoles().contains(event.getGuild().getRoleById(1065671698688512090L))) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.whop.com/api/v2/promo_codes?expand="))
                    .header("accept", "application/json")
                    .header("Authorization", Config.get("whop"))
                    .header("content-type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString("{\"new_users_only\":true,\"number_of_intervals\":1,\"amount_off\":100,\"code\":\"" + event.getUser().getId() + "\",\"base_currency\":\"usd\",\"promo_type\":\"percentage\",\"stock\":1000000000}"))
                    .build();
            HttpResponse<String> response;
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                Util.sendDM(event.getUser().getId(), "Congratulations on reaching Level 3! Here is your personal discount code `" + event.getUser().getId() + "` which is a one time use code that gives any user the first month free. This will be needed to complete the task to get `L3e`.");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
