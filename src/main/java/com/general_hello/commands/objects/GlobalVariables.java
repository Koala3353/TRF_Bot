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
    public static long CHAMPS = Long.parseLong(Config.get("champs"));
    public static String VERSION = "1.2.1";
}
