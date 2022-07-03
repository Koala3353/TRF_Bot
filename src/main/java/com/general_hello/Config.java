package com.general_hello;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    // Simple Config getter (.env file)
    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
    }
    public static long getLong(String key) {
        return Long.parseLong(dotenv.get(key.toUpperCase()));
    }
}
