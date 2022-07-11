package com.general_hello.commands.objects;

import com.general_hello.Config;

/**
 * <p>Make a <b>.env</b> file with the following contents and replace the <b>?</b> with the proper values</p>
 */
/*
    # Bot token
    TOKEN=?
    # Your id
    OWNER_ID=?
    # Bot Prefix
    PREFIX=/
    # Partner owner id
    OWNER_ID_PARTNER=?
    # Server ID
    GUILD=?
    # Link of the hyperlinks
    LINK=?
    # Rapid API key (https://rapidapi.com/theoddsapi/api/live-sports-odds/)
    KEY=?
 */
public class GlobalVariables {
    public static String API_KEY = Config.get("key");
    public static String LINK = Config.get("link");
    public static String BOOKMAKERS = Config.get("bookmakers");
    public static String CHAMPS = Config.get("champ_prefix").toLowerCase();
    public static String HALL_OF_FAME = Config.get("hall_of_fame");
    public static String VERSION = "2.1.0";
}
