package com.general_hello.bot.utils;

import com.general_hello.bot.objects.SportType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    public static void writeJsonTemplate() {
        //First Employee
        JSONArray finalArray = new JSONArray();
        for (SportType sportType : SportType.values()) {
            JSONObject sportObject = new JSONObject();
            sportObject.put("sport", sportType.getName());
            JSONArray jsonArray = new JSONArray();
            JSONObject user = new JSONObject();
            user.put("userid", 0L);
            user.put("interactionCount", 0);
            user.put("successPred", 0);
            user.put("failPred", 0);
            jsonArray.put(user);
            sportObject.put("users", jsonArray);
            finalArray.put(sportObject);
        }

        //Write JSON file
        try (FileWriter file = new FileWriter("records.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(finalArray.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOGGER.info("Made a new records.json file");
    }

    public static void incrementInteraction(String sport, String userId) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get("records.json")));
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject sportObject = array.getJSONObject(i);
                if (sportObject.getString("sport").equals(sport)) {
                    JSONArray users = sportObject.getJSONArray("users");
                    for (int j = 0; j < users.length(); j++) {
                        JSONObject user = users.getJSONObject(j);
                        if (user.getLong("userid") == Long.parseLong(userId)) {
                            user.put("interactionCount", user.getInt("interactionCount") + 1);
                            LOGGER.info("Incremented interaction count for user ({})", userId);
                            //Write JSON file
                            try (FileWriter file = new FileWriter("records.json")) {
                                //We can write any JSONArray or JSONObject instance to the file
                                file.write(array.toString());
                                file.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    JSONObject newUser = new JSONObject();
                    newUser.put("userid", userId);
                    newUser.put("interactionCount", 1);
                    newUser.put("successPred", 0);
                    newUser.put("failPred", 0);
                    users.put(newUser);
                    //Write JSON file
                    try (FileWriter file = new FileWriter("records.json")) {
                        //We can write any JSONArray or JSONObject instance to the file
                        file.write(array.toString());
                        file.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("Added new user (" + userId + ") " + "to the json");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void incrementCorrectPred(String sport, long userId) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get("records.json")));
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject sportObject = array.getJSONObject(i);
                if (sportObject.getString("sport").equals(sport)) {
                    JSONArray users = sportObject.getJSONArray("users");
                    for (int j = 0; j < users.length(); j++) {
                        JSONObject user = users.getJSONObject(j);
                        if (user.getLong("userid") == userId) {
                            user.put("successPred", user.getInt("successPred") + 1);
                            LOGGER.info("Incremented correct prediction count for user ({})", userId);
                            //Write JSON file
                            try (FileWriter file = new FileWriter("records.json")) {
                                //We can write any JSONArray or JSONObject instance to the file
                                file.write(array.toString());
                                file.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    JSONObject newUser = new JSONObject();
                    newUser.put("userid", userId);
                    newUser.put("interactionCount", 0);
                    newUser.put("successPred", 1);
                    newUser.put("failPred", 0);
                    users.put(newUser);
                    //Write JSON file
                    try (FileWriter file = new FileWriter("records.json")) {
                        //We can write any JSONArray or JSONObject instance to the file
                        file.write(array.toString());
                        file.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("Added new user (" + userId + ") " + "to the json");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void incrementWrongPred(String sport, long userId) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get("records.json")));
            JSONArray array = new JSONArray(jsonString);
            for (int i = 0; i < array.length(); i++) {
                JSONObject sportObject = array.getJSONObject(i);
                if (sportObject.getString("sport").equals(sport)) {
                    JSONArray users = sportObject.getJSONArray("users");
                    for (int j = 0; j < users.length(); j++) {
                        JSONObject user = users.getJSONObject(j);
                        if (user.getLong("userid") == userId) {
                            user.put("failPred", user.getInt("failPred") + 1);
                            LOGGER.info("Incremented incorrect prediction count for user ({})", userId);
                            //Write JSON file
                            try (FileWriter file = new FileWriter("records.json")) {
                                //We can write any JSONArray or JSONObject instance to the file
                                file.write(array.toString());
                                file.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
                    }
                    JSONObject newUser = new JSONObject();
                    newUser.put("userid", userId);
                    newUser.put("interactionCount", 0);
                    newUser.put("successPred", 0);
                    newUser.put("failPred", 1);
                    users.put(newUser);
                    //Write JSON file
                    try (FileWriter file = new FileWriter("records.json")) {
                        //We can write any JSONArray or JSONObject instance to the file
                        file.write(array.toString());
                        file.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("Added new user (" + userId + ") " + "to the json");
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
